package com.jie.graduationproject.service.InventoryLocation.Impl;

import com.jie.graduationproject.model.dto.AddGoodsWithLocationDTO;
import com.jie.graduationproject.model.dto.InventoryLocationDTO;
import com.jie.graduationproject.model.entity.Goods;
import com.jie.graduationproject.model.entity.InventoryLocation;
import com.jie.graduationproject.model.entity.OperationLog;
import com.jie.graduationproject.model.entity.Shelf;
import com.jie.graduationproject.model.entity.ShelfLevel;
import com.jie.graduationproject.repository.GoodsRepository;
import com.jie.graduationproject.repository.InventoryLocationRepository;
import com.jie.graduationproject.repository.OperationLogRepository;
import com.jie.graduationproject.repository.ShelfLevelRepository;
import com.jie.graduationproject.service.InventoryLocation.InventoryLocationService;
import com.jie.graduationproject.service.ShelfLevel.ShelfLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryLocationServiceImpl implements InventoryLocationService {

    private final InventoryLocationRepository inventoryLocationRepository;
    private final GoodsRepository goodsRepository;
    private final ShelfLevelRepository shelfLevelRepository;
    private final ShelfLevelService shelfLevelService;
    private final OperationLogRepository operationLogRepository;

    @Autowired
    public InventoryLocationServiceImpl(
            InventoryLocationRepository inventoryLocationRepository,
            GoodsRepository goodsRepository,
            ShelfLevelRepository shelfLevelRepository,
            ShelfLevelService shelfLevelService,
            OperationLogRepository operationLogRepository) {
        this.inventoryLocationRepository = inventoryLocationRepository;
        this.goodsRepository = goodsRepository;
        this.shelfLevelRepository = shelfLevelRepository;
        this.shelfLevelService = shelfLevelService;
        this.operationLogRepository = operationLogRepository;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "系统";
    }

    private String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .findFirst()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .orElse("未知");
        }
        return "系统";
    }

    private String buildLocationInfo(InventoryLocation location) {
        if (location == null || location.getShelfLevel() == null) return "";
        ShelfLevel level = location.getShelfLevel();
        Shelf shelf = level.getShelf();
        return (shelf != null ? shelf.getShelfName() + " - " : "") + level.getLevelName() +
                (location.getPosition() != null ? " (" + location.getPosition() + ")" : "");
    }

    private void saveOperationLog(String operationType, String description, String goodsName,
                                  Integer quantity, String locationInfo) {
        OperationLog log = new OperationLog(operationType, description, goodsName, quantity,
                getCurrentUsername(), getCurrentUserRole(), locationInfo);
        operationLogRepository.save(log);
    }

    @Override
    public ResponseEntity<?> addGoodsWithLocation(AddGoodsWithLocationDTO request) {
        try {
            // 验证请求
            if (request == null || request.getGoods() == null || request.getLocations() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("请求数据不完整");
            }
            
            if (!request.validateTotalQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("总数量必须大于0");
            }
            
            AddGoodsWithLocationDTO.GoodsInfo goodsInfo = request.getGoods();
            
            // 检查商品是否已存在
            Optional<Goods> existingGoods = goodsRepository.findBySku(goodsInfo.getSku());
            Goods goods;
            
            if (existingGoods.isPresent()) {
                // 商品已存在，使用现有商品
                goods = existingGoods.get();
            } else {
                // 创建新商品
                goods = new Goods();
                goods.setSku(goodsInfo.getSku());
                goods.setName(goodsInfo.getName());
                goods.setStorageTemperature(goodsInfo.getStorageTemperature());
                goods.setStatus(goodsInfo.getStatus());
                goods.setStorageDate(goodsInfo.getStorageDate());
                goods.setExpiryDate(goodsInfo.getExpiryDate());
                goods.setSupplierName(goodsInfo.getSupplierName());
                goods.setSupplierContact(goodsInfo.getSupplierContact());
                goods.setCreatedAt(LocalDateTime.now());
                goods.setUpdatedAt(LocalDateTime.now());
                
                goods = goodsRepository.save(goods);
            }
            
            // 处理每个存放位置
            List<InventoryLocation> savedLocations = new ArrayList<>();
            int totalQuantity = 0;
            
            for (AddGoodsWithLocationDTO.LocationInfo locationInfo : request.getLocations()) {
                // 检查货架层是否存在
                Optional<ShelfLevel> optionalLevel = shelfLevelRepository.findById(locationInfo.getShelfLevelId());
                if (optionalLevel.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("货架层不存在: " + locationInfo.getShelfLevelId());
                }
                
                ShelfLevel shelfLevel = optionalLevel.get();
                
                // 检查容量
                if (!shelfLevelService.checkLevelCapacity(shelfLevel.getId(), locationInfo.getQuantity())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("货架层容量不足: " + shelfLevel.getLevelName());
                }
                
                // 创建库存位置记录
                InventoryLocation inventoryLocation = new InventoryLocation();
                inventoryLocation.setGoods(goods);
                inventoryLocation.setShelfLevel(shelfLevel);
                inventoryLocation.setQuantity(locationInfo.getQuantity());
                inventoryLocation.setPosition(locationInfo.getPosition());
                inventoryLocation.setBatchNumber(locationInfo.getBatchNumber());
                inventoryLocation.setStorageDate(locationInfo.getStorageDate() != null ? 
                        locationInfo.getStorageDate() : goodsInfo.getStorageDate());
                inventoryLocation.setExpiryDate(locationInfo.getExpiryDate() != null ? 
                        locationInfo.getExpiryDate() : goodsInfo.getExpiryDate());
                inventoryLocation.setStatus("正常");
                inventoryLocation.setCreatedAt(LocalDateTime.now());
                inventoryLocation.setUpdatedAt(LocalDateTime.now());
                
                InventoryLocation savedLocation = inventoryLocationRepository.save(inventoryLocation);
                savedLocations.add(savedLocation);
                
                // 更新货架层当前数量
                shelfLevelService.updateLevelQuantity(shelfLevel.getId(), locationInfo.getQuantity());
                
                totalQuantity += locationInfo.getQuantity();
            }
            
            // 更新商品总库存
            if (goods.getQuantity() == null) {
                goods.setQuantity(totalQuantity);
            } else {
                goods.setQuantity(goods.getQuantity() + totalQuantity);
            }
            goods.setUpdatedAt(LocalDateTime.now());
            goodsRepository.save(goods);
            
            // 返回结果
            List<InventoryLocationDTO> locationDTOs = savedLocations.stream()
                    .map(InventoryLocationDTO::fromEntity)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("goods", goods);
            response.put("locations", locationDTOs);
            response.put("totalQuantity", totalQuantity);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加商品失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getGoodsLocations(Long goodsId) {
        try {
            // 检查商品是否存在
            Optional<Goods> optionalGoods = goodsRepository.findById(goodsId);
            if (optionalGoods.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("商品不存在");
            }
            
            Goods goods = optionalGoods.get();
            
            // 获取所有库存位置
            List<InventoryLocation> locations = inventoryLocationRepository.findByGoodsIdAndStatus(goodsId, "正常");
            List<InventoryLocationDTO> locationDTOs = locations.stream()
                    .map(InventoryLocationDTO::fromEntity)
                    .collect(Collectors.toList());
            
            // 计算总库存
            Integer totalStock = inventoryLocationRepository.sumQuantityByGoodsId(goodsId);
            if (totalStock == null) {
                totalStock = 0;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("goods", goods);
            response.put("locations", locationDTOs);
            response.put("totalStock", totalStock);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取商品库存位置失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getLevelInventory(Long shelfLevelId) {
        try {
            // 检查货架层是否存在
            Optional<ShelfLevel> optionalLevel = shelfLevelRepository.findById(shelfLevelId);
            if (optionalLevel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架层不存在");
            }
            
            ShelfLevel shelfLevel = optionalLevel.get();
            
            // 获取该层的所有库存
            List<InventoryLocation> locations = inventoryLocationRepository.findByShelfLevelIdAndStatus(shelfLevelId, "正常");
            List<InventoryLocationDTO> locationDTOs = locations.stream()
                    .map(InventoryLocationDTO::fromEntity)
                    .collect(Collectors.toList());
            
            // 按商品分组统计
            Map<Long, Map<String, Object>> goodsSummary = new HashMap<>();
            for (InventoryLocation location : locations) {
                Long goodsId = location.getGoods().getId();
                String goodsName = location.getGoods().getName();
                
                if (!goodsSummary.containsKey(goodsId)) {
                    Map<String, Object> summary = new HashMap<>();
                    summary.put("goodsId", goodsId);
                    summary.put("goodsName", goodsName);
                    summary.put("totalQuantity", 0);
                    summary.put("locations", new ArrayList<InventoryLocationDTO>());
                    goodsSummary.put(goodsId, summary);
                }
                
                Map<String, Object> summary = goodsSummary.get(goodsId);
                summary.put("totalQuantity", (Integer)summary.get("totalQuantity") + location.getQuantity());
                ((List<InventoryLocationDTO>)summary.get("locations")).add(InventoryLocationDTO.fromEntity(location));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("shelfLevel", shelfLevel);
            response.put("inventory", locationDTOs);
            response.put("goodsSummary", new ArrayList<>(goodsSummary.values()));
            response.put("totalItems", locations.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取货架层库存失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> moveInventory(Long fromLocationId, Long toShelfLevelId, Integer quantity, String position) {
        try {
            // 检查源库存位置
            Optional<InventoryLocation> optionalFromLocation = inventoryLocationRepository.findById(fromLocationId);
            if (optionalFromLocation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("源库存位置不存在");
            }
            
            InventoryLocation fromLocation = optionalFromLocation.get();
            
            // 检查数量
            if (quantity <= 0 || quantity > fromLocation.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("移动数量无效");
            }
            
            // 检查目标货架层
            Optional<ShelfLevel> optionalToLevel = shelfLevelRepository.findById(toShelfLevelId);
            if (optionalToLevel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("目标货架层不存在");
            }
            
            ShelfLevel toLevel = optionalToLevel.get();
            
            // 检查目标容量
            if (!shelfLevelService.checkLevelCapacity(toLevel.getId(), quantity)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("目标货架层容量不足");
            }
            
            // 减少源库存
            fromLocation.setQuantity(fromLocation.getQuantity() - quantity);
            fromLocation.setUpdatedAt(LocalDateTime.now());
            
            if (fromLocation.getQuantity() == 0) {
                fromLocation.setStatus("已出库");
            }
            
            inventoryLocationRepository.save(fromLocation);
            
            // 更新源货架层数量
            shelfLevelService.updateLevelQuantity(fromLocation.getShelfLevel().getId(), -quantity);
            
            // 创建或更新目标库存位置
            List<InventoryLocation> existingLocations = inventoryLocationRepository.findByGoodsIdAndShelfLevelIdAndStatus(
                    fromLocation.getGoods().getId(), toLevel.getId(), "正常");
            
            InventoryLocation toLocation;
            if (!existingLocations.isEmpty()) {
                // 合并到现有库存
                toLocation = existingLocations.get(0);
                toLocation.setQuantity(toLocation.getQuantity() + quantity);
                toLocation.setUpdatedAt(LocalDateTime.now());
            } else {
                // 创建新库存记录
                toLocation = new InventoryLocation();
                toLocation.setGoods(fromLocation.getGoods());
                toLocation.setShelfLevel(toLevel);
                toLocation.setQuantity(quantity);
                toLocation.setPosition(position);
                toLocation.setBatchNumber(fromLocation.getBatchNumber());
                toLocation.setStorageDate(fromLocation.getStorageDate());
                toLocation.setExpiryDate(fromLocation.getExpiryDate());
                toLocation.setStatus("正常");
                toLocation.setCreatedAt(LocalDateTime.now());
                toLocation.setUpdatedAt(LocalDateTime.now());
            }
            
            InventoryLocation savedToLocation = inventoryLocationRepository.save(toLocation);
            
            // 更新目标货架层数量
            shelfLevelService.updateLevelQuantity(toLevel.getId(), quantity);
            
            // 记录操作日志
            String fromInfo = buildLocationInfo(fromLocation);
            String toInfo = buildLocationInfo(savedToLocation);
            String goodsName = fromLocation.getGoods().getName();
            String description = "将 " + goodsName + " x" + quantity + " 从 " + fromInfo + " 移至 " + toInfo;
            saveOperationLog("移库", description, goodsName, quantity, fromInfo + " → " + toInfo);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "库存移动成功");
            response.put("fromLocation", InventoryLocationDTO.fromEntity(fromLocation));
            response.put("toLocation", InventoryLocationDTO.fromEntity(savedToLocation));
            response.put("quantity", quantity);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("移动库存失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> outboundInventory(Long locationId, Integer quantity) {
        try {
            // 检查库存位置
            Optional<InventoryLocation> optionalLocation = inventoryLocationRepository.findById(locationId);
            if (optionalLocation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("库存位置不存在");
            }
            
            InventoryLocation location = optionalLocation.get();
            
            // 检查数量
            if (quantity <= 0 || quantity > location.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("出库数量无效");
            }
            
            // 减少库存
            location.setQuantity(location.getQuantity() - quantity);
            location.setUpdatedAt(LocalDateTime.now());
            
            if (location.getQuantity() == 0) {
                location.setStatus("已出库");
            }
            
            inventoryLocationRepository.save(location);
            
            // 更新货架层数量
            shelfLevelService.updateLevelQuantity(location.getShelfLevel().getId(), -quantity);
            
            // 更新商品总库存
            Goods goods = location.getGoods();
            goods.setQuantity(goods.getQuantity() - quantity);
            goods.setUpdatedAt(LocalDateTime.now());
            goodsRepository.save(goods);
            
            // 记录操作日志
            String goodsName = location.getGoods().getName();
            String locInfo = buildLocationInfo(location);
            String description = "出库 " + goodsName + " x" + quantity + "，剩余 " + location.getQuantity();
            saveOperationLog("出库", description, goodsName, -quantity, locInfo);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "出库成功");
            response.put("location", InventoryLocationDTO.fromEntity(location));
            response.put("quantity", quantity);
            response.put("remaining", location.getQuantity());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("出库失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> inboundInventory(Long locationId, Integer quantity) {
        try {
            // 检查库存位置
            Optional<InventoryLocation> optionalLocation = inventoryLocationRepository.findById(locationId);
            if (optionalLocation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("库存位置不存在");
            }
            
            InventoryLocation location = optionalLocation.get();
            
            // 检查数量
            if (quantity <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("入库数量必须大于0");
            }
            
            // 检查货架层容量
            ShelfLevel shelfLevel = location.getShelfLevel();
            if (!shelfLevelService.checkLevelCapacity(shelfLevel.getId(), quantity)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("货架层容量不足");
            }
            
            // 增加库存
            location.setQuantity(location.getQuantity() + quantity);
            location.setUpdatedAt(LocalDateTime.now());
            
            inventoryLocationRepository.save(location);
            
            // 更新货架层数量
            shelfLevelService.updateLevelQuantity(shelfLevel.getId(), quantity);
            
            // 更新商品总库存
            Goods goods = location.getGoods();
            goods.setQuantity(goods.getQuantity() + quantity);
            goods.setUpdatedAt(LocalDateTime.now());
            goodsRepository.save(goods);
            
            // 记录操作日志
            String goodsName = location.getGoods().getName();
            String locInfo = buildLocationInfo(location);
            String description = "入库 " + goodsName + " x" + quantity + "，当前总计 " + location.getQuantity();
            saveOperationLog("入库", description, goodsName, quantity, locInfo);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "入库成功");
            response.put("location", InventoryLocationDTO.fromEntity(location));
            response.put("quantity", quantity);
            response.put("total", location.getQuantity());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("入库失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateInventoryLocation(Long locationId, InventoryLocationDTO locationDTO) {
        try {
            // 检查库存位置
            Optional<InventoryLocation> optionalLocation = inventoryLocationRepository.findById(locationId);
            if (optionalLocation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("库存位置不存在");
            }
            
            InventoryLocation location = optionalLocation.get();
            
            // 检查货架层是否存在（如果更新了货架层）
            if (locationDTO.getShelfLevelId() != null && !locationDTO.getShelfLevelId().equals(location.getShelfLevel().getId())) {
                Optional<ShelfLevel> optionalNewLevel = shelfLevelRepository.findById(locationDTO.getShelfLevelId());
                if (optionalNewLevel.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("新货架层不存在");
                }
                
                ShelfLevel newLevel = optionalNewLevel.get();
                
                // 检查新货架层容量
                if (!shelfLevelService.checkLevelCapacity(newLevel.getId(), location.getQuantity())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("新货架层容量不足");
                }
                
                // 更新原货架层数量
                shelfLevelService.updateLevelQuantity(location.getShelfLevel().getId(), -location.getQuantity());
                
                // 设置新货架层
                location.setShelfLevel(newLevel);
                
                // 更新新货架层数量
                shelfLevelService.updateLevelQuantity(newLevel.getId(), location.getQuantity());
            }
            
            // 更新其他字段
            if (locationDTO.getPosition() != null) {
                location.setPosition(locationDTO.getPosition());
            }
            
            if (locationDTO.getBatchNumber() != null) {
                location.setBatchNumber(locationDTO.getBatchNumber());
            }
            
            if (locationDTO.getStorageDate() != null) {
                location.setStorageDate(locationDTO.getStorageDate());
            }
            
            if (locationDTO.getExpiryDate() != null) {
                location.setExpiryDate(locationDTO.getExpiryDate());
            }
            
            if (locationDTO.getStatus() != null) {
                location.setStatus(locationDTO.getStatus());
            }
            
            location.setUpdatedAt(LocalDateTime.now());
            
            InventoryLocation updatedLocation = inventoryLocationRepository.save(location);
            
            return ResponseEntity.ok(InventoryLocationDTO.fromEntity(updatedLocation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新库存位置失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteInventoryLocation(Long locationId) {
        try {
            // 检查库存位置
            Optional<InventoryLocation> optionalLocation = inventoryLocationRepository.findById(locationId);
            if (optionalLocation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("库存位置不存在");
            }
            
            InventoryLocation location = optionalLocation.get();
            
            // 检查是否有库存
            if (location.getQuantity() > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("无法删除有库存的位置，请先出库所有库存");
            }
            
            // 更新货架层数量（如果之前有库存）
            if (location.getQuantity() > 0) {
                shelfLevelService.updateLevelQuantity(location.getShelfLevel().getId(), -location.getQuantity());
            }
            
            // 更新商品总库存
            Goods goods = location.getGoods();
            goods.setQuantity(goods.getQuantity() - location.getQuantity());
            goods.setUpdatedAt(LocalDateTime.now());
            goodsRepository.save(goods);
            
            // 删除库存位置
            inventoryLocationRepository.delete(location);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "删除库存位置成功");
            response.put("deletedLocationId", locationId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除库存位置失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getExpiringInventory(Integer days) {
        try {
            LocalDate expiryThreshold = LocalDate.now().plusDays(days);
            
            // 获取即将过期的库存
            List<InventoryLocation> expiringLocations = inventoryLocationRepository.findByExpiryDateBeforeAndStatus(
                    expiryThreshold, "正常");
            
            // 按商品分组
            Map<Long, Map<String, Object>> goodsMap = new HashMap<>();
            for (InventoryLocation location : expiringLocations) {
                Long goodsId = location.getGoods().getId();
                String goodsName = location.getGoods().getName();
                
                if (!goodsMap.containsKey(goodsId)) {
                    Map<String, Object> goodsInfo = new HashMap<>();
                    goodsInfo.put("goodsId", goodsId);
                    goodsInfo.put("goodsName", goodsName);
                    goodsInfo.put("totalQuantity", 0);
                    goodsInfo.put("locations", new ArrayList<InventoryLocationDTO>());
                    goodsInfo.put("earliestExpiryDate", location.getExpiryDate());
                    goodsMap.put(goodsId, goodsInfo);
                }
                
                Map<String, Object> goodsInfo = goodsMap.get(goodsId);
                goodsInfo.put("totalQuantity", (Integer)goodsInfo.get("totalQuantity") + location.getQuantity());
                ((List<InventoryLocationDTO>)goodsInfo.get("locations")).add(InventoryLocationDTO.fromEntity(location));
                
                // 更新最早过期日期
                LocalDate currentEarliest = (LocalDate) goodsInfo.get("earliestExpiryDate");
                if (location.getExpiryDate().isBefore(currentEarliest)) {
                    goodsInfo.put("earliestExpiryDate", location.getExpiryDate());
                }
            }
            
            // 转换为列表并排序（按最早过期日期）
            List<Map<String, Object>> expiringGoods = new ArrayList<>(goodsMap.values());
            expiringGoods.sort((a, b) -> ((LocalDate) a.get("earliestExpiryDate"))
                    .compareTo((LocalDate) b.get("earliestExpiryDate")));
            
            Map<String, Object> response = new HashMap<>();
            response.put("expiringGoods", expiringGoods);
            response.put("totalItems", expiringLocations.size());
            response.put("expiryThreshold", expiryThreshold);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取即将过期库存失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getFIFOOutboundSuggestions(Long goodsId, Integer requiredQuantity) {
        try {
            // 检查商品是否存在
            Optional<Goods> optionalGoods = goodsRepository.findById(goodsId);
            if (optionalGoods.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("商品不存在");
            }
            
            // 获取按先进先出排序的库存
            List<InventoryLocation> inventoryLocations = inventoryLocationRepository
                    .findInventoryByGoodsIdOrderByStorageDate(goodsId);
            
            if (inventoryLocations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("该商品没有库存");
            }
            
            // 计算总可用库存
            int totalAvailable = inventoryLocations.stream()
                    .mapToInt(InventoryLocation::getQuantity)
                    .sum();
            
            if (totalAvailable < requiredQuantity) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("库存不足，可用库存: " + totalAvailable);
            }
            
            // 生成出库建议
            List<Map<String, Object>> suggestions = new ArrayList<>();
            int remainingQuantity = requiredQuantity;
            
            for (InventoryLocation location : inventoryLocations) {
                if (remainingQuantity <= 0) {
                    break;
                }
                
                int availableAtLocation = location.getQuantity();
                int quantityToTake = Math.min(availableAtLocation, remainingQuantity);
                
                if (quantityToTake > 0) {
                    Map<String, Object> suggestion = new HashMap<>();
                    suggestion.put("locationId", location.getId());
                    suggestion.put("shelfLevelId", location.getShelfLevel().getId());
                    suggestion.put("shelfLevelName", location.getShelfLevel().getLevelName());
                    suggestion.put("position", location.getPosition());
                    suggestion.put("batchNumber", location.getBatchNumber());
                    suggestion.put("storageDate", location.getStorageDate());
                    suggestion.put("expiryDate", location.getExpiryDate());
                    suggestion.put("availableQuantity", availableAtLocation);
                    suggestion.put("suggestedQuantity", quantityToTake);
                    
                    suggestions.add(suggestion);
                    remainingQuantity -= quantityToTake;
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("goodsId", goodsId);
            response.put("goodsName", optionalGoods.get().getName());
            response.put("requiredQuantity", requiredQuantity);
            response.put("suggestions", suggestions);
            response.put("totalSuggested", requiredQuantity - remainingQuantity);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取出库建议失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getInventoryStatistics() {
        try {
            // 获取所有正常状态的库存
            List<InventoryLocation> allLocations = inventoryLocationRepository.findAll();
            
            // 基本统计
            int totalLocations = allLocations.size();
            int totalQuantity = allLocations.stream()
                    .filter(loc -> "正常".equals(loc.getStatus()))
                    .mapToInt(InventoryLocation::getQuantity)
                    .sum();
            
            // 按商品统计
            Map<Long, Map<String, Object>> goodsStats = new HashMap<>();
            for (InventoryLocation location : allLocations) {
                if (!"正常".equals(location.getStatus())) {
                    continue;
                }
                
                Long goodsId = location.getGoods().getId();
                String goodsName = location.getGoods().getName();
                
                if (!goodsStats.containsKey(goodsId)) {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("goodsId", goodsId);
                    stats.put("goodsName", goodsName);
                    stats.put("totalQuantity", 0);
                    stats.put("locationCount", 0);
                    stats.put("locations", new ArrayList<InventoryLocationDTO>());
                    goodsStats.put(goodsId, stats);
                }
                
                Map<String, Object> stats = goodsStats.get(goodsId);
                stats.put("totalQuantity", (Integer)stats.get("totalQuantity") + location.getQuantity());
                stats.put("locationCount", (Integer)stats.get("locationCount") + 1);
                ((List<InventoryLocationDTO>)stats.get("locations")).add(InventoryLocationDTO.fromEntity(location));
            }
            
            // 按区域统计
            Map<String, Integer> areaStats = new HashMap<>();
            for (InventoryLocation location : allLocations) {
                if (!"正常".equals(location.getStatus())) {
                    continue;
                }
                
                String area = location.getShelfLevel().getShelf().getArea();
                areaStats.put(area, areaStats.getOrDefault(area, 0) + location.getQuantity());
            }
            
            // 即将过期统计
            LocalDate nextWeek = LocalDate.now().plusDays(7);
            List<InventoryLocation> expiringLocations = inventoryLocationRepository
                    .findByExpiryDateBeforeAndStatus(nextWeek, "正常");
            int expiringQuantity = expiringLocations.stream()
                    .mapToInt(InventoryLocation::getQuantity)
                    .sum();
            
            // 货架层使用率
            List<ShelfLevel> allLevels = shelfLevelRepository.findAll();
            int totalCapacity = allLevels.stream()
                    .mapToInt(ShelfLevel::getCapacity)
                    .sum();
            int usedCapacity = allLevels.stream()
                    .mapToInt(ShelfLevel::getCurrentQuantity)
                    .sum();
            double utilizationRate = totalCapacity > 0 ? (double) usedCapacity / totalCapacity * 100 : 0;
            
            Map<String, Object> response = new HashMap<>();
            response.put("totalLocations", totalLocations);
            response.put("totalQuantity", totalQuantity);
            response.put("totalGoods", goodsStats.size());
            response.put("goodsStatistics", new ArrayList<>(goodsStats.values()));
            response.put("areaStatistics", areaStats);
            response.put("expiringQuantity", expiringQuantity);
            response.put("expiringLocations", expiringLocations.size());
            response.put("totalCapacity", totalCapacity);
            response.put("usedCapacity", usedCapacity);
            response.put("utilizationRate", String.format("%.2f%%", utilizationRate));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取库存统计失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> searchInventory(String keyword, String area, String shelfType, LocalDate expiryDate) {
        try {
            List<InventoryLocation> results = new ArrayList<>();
            
            // 根据搜索条件获取库存
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 搜索商品名称或SKU
                List<Goods> goodsList = goodsRepository.findAll().stream()
                        .filter(g -> g.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                   g.getSku().toLowerCase().contains(keyword.toLowerCase()))
                        .collect(Collectors.toList());
                
                for (Goods goods : goodsList) {
                    results.addAll(inventoryLocationRepository.findByGoodsIdAndStatus(goods.getId(), "正常"));
                }
            } else if (area != null && !area.trim().isEmpty()) {
                // 按区域搜索
                results = inventoryLocationRepository.findByArea(area);
            } else if (shelfType != null && !shelfType.trim().isEmpty()) {
                // 按货架类型搜索
                results = inventoryLocationRepository.findByShelfType(shelfType);
            } else if (expiryDate != null) {
                // 按过期日期搜索
                results = inventoryLocationRepository.findByExpiryDateBeforeAndStatus(expiryDate, "正常");
            } else {
                // 如果没有搜索条件，返回所有正常库存
                results = inventoryLocationRepository.findAll().stream()
                        .filter(loc -> "正常".equals(loc.getStatus()))
                        .collect(Collectors.toList());
            }
            
            // 过滤结果（如果同时有多个条件）
            List<InventoryLocation> filteredResults = results.stream()
                    .filter(loc -> {
                        boolean matches = true;
                        
                        if (area != null && !area.trim().isEmpty()) {
                            matches = matches && loc.getShelfLevel().getShelf().getArea().equals(area);
                        }
                        
                        if (shelfType != null && !shelfType.trim().isEmpty()) {
                            matches = matches && loc.getShelfLevel().getShelf().getShelfType().equals(shelfType);
                        }
                        
                        if (expiryDate != null) {
                            matches = matches && loc.getExpiryDate() != null && 
                                     loc.getExpiryDate().isBefore(expiryDate.plusDays(1));
                        }
                        
                        return matches;
                    })
                    .collect(Collectors.toList());
            
            // 转换为DTO
            List<InventoryLocationDTO> resultDTOs = filteredResults.stream()
                    .map(InventoryLocationDTO::fromEntity)
                    .collect(Collectors.toList());
            
            // 按商品分组统计
            Map<Long, Map<String, Object>> goodsSummary = new HashMap<>();
            for (InventoryLocation location : filteredResults) {
                Long goodsId = location.getGoods().getId();
                String goodsName = location.getGoods().getName();
                
                if (!goodsSummary.containsKey(goodsId)) {
                    Map<String, Object> summary = new HashMap<>();
                    summary.put("goodsId", goodsId);
                    summary.put("goodsName", goodsName);
                    summary.put("totalQuantity", 0);
                    summary.put("locationCount", 0);
                    goodsSummary.put(goodsId, summary);
                }
                
                Map<String, Object> summary = goodsSummary.get(goodsId);
                summary.put("totalQuantity", (Integer)summary.get("totalQuantity") + location.getQuantity());
                summary.put("locationCount", (Integer)summary.get("locationCount") + 1);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("results", resultDTOs);
            response.put("totalResults", filteredResults.size());
            response.put("goodsSummary", new ArrayList<>(goodsSummary.values()));
            response.put("searchCriteria", Map.of(
                "keyword", keyword,
                "area", area,
                "shelfType", shelfType,
                "expiryDate", expiryDate
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("搜索库存失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> batchUpdateStatus(List<Long> locationIds, String status) {
        try {
            if (locationIds == null || locationIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("请选择要更新的库存位置");
            }
            
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("请指定状态");
            }
            
            // 验证状态值
            List<String> validStatuses = Arrays.asList("正常", "已出库", "盘点中", "冻结", "损坏");
            if (!validStatuses.contains(status)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("无效的状态值，可选值: " + validStatuses);
            }
            
            // 批量更新状态
            int updatedCount = 0;
            List<InventoryLocation> updatedLocations = new ArrayList<>();
            
            for (Long locationId : locationIds) {
                Optional<InventoryLocation> optionalLocation = inventoryLocationRepository.findById(locationId);
                if (optionalLocation.isPresent()) {
                    InventoryLocation location = optionalLocation.get();
                    
                    // 记录旧状态
                    String oldStatus = location.getStatus();
                    
                    // 更新状态
                    location.setStatus(status);
                    location.setUpdatedAt(LocalDateTime.now());
                    
                    InventoryLocation updatedLocation = inventoryLocationRepository.save(location);
                    updatedLocations.add(updatedLocation);
                    updatedCount++;
                    
                    // 如果状态变为"已出库"，需要更新货架层数量
                    if ("已出库".equals(status) && !"已出库".equals(oldStatus)) {
                        shelfLevelService.updateLevelQuantity(
                            location.getShelfLevel().getId(), 
                            -location.getQuantity()
                        );
                        
                        // 更新商品总库存
                        Goods goods = location.getGoods();
                        goods.setQuantity(goods.getQuantity() - location.getQuantity());
                        goods.setUpdatedAt(LocalDateTime.now());
                        goodsRepository.save(goods);
                    }
                    
                    // 如果从"已出库"变为其他状态，需要恢复货架层数量
                    if ("已出库".equals(oldStatus) && !"已出库".equals(status)) {
                        shelfLevelService.updateLevelQuantity(
                            location.getShelfLevel().getId(), 
                            location.getQuantity()
                        );
                        
                        // 恢复商品总库存
                        Goods goods = location.getGoods();
                        goods.setQuantity(goods.getQuantity() + location.getQuantity());
                        goods.setUpdatedAt(LocalDateTime.now());
                        goodsRepository.save(goods);
                    }
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "批量更新状态成功");
            response.put("updatedCount", updatedCount);
            response.put("totalRequested", locationIds.size());
            response.put("newStatus", status);
            response.put("updatedLocations", updatedLocations.stream()
                    .map(InventoryLocationDTO::fromEntity)
                    .collect(Collectors.toList()));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("批量更新状态失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> createInventoryLocation(Long goodsId, Long shelfLevelId, Integer quantity,
                                                    String position, String batchNumber,
                                                    String storageDateStr, String expiryDateStr) {
        try {
            // 检查商品是否存在
            Optional<Goods> optionalGoods = goodsRepository.findById(goodsId);
            if (optionalGoods.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("商品不存在");
            }
            
            // 检查货架层是否存在
            Optional<ShelfLevel> optionalLevel = shelfLevelRepository.findById(shelfLevelId);
            if (optionalLevel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架层不存在");
            }
            
            Goods goods = optionalGoods.get();
            ShelfLevel shelfLevel = optionalLevel.get();
            
            // 检查数量
            if (quantity <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("入库数量必须大于0");
            }
            
            // 检查货架层容量
            if (!shelfLevelService.checkLevelCapacity(shelfLevelId, quantity)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("货架层容量不足");
            }
            
            // 创建库存位置
            InventoryLocation location = new InventoryLocation();
            location.setGoods(goods);
            location.setShelfLevel(shelfLevel);
            location.setQuantity(quantity);
            location.setPosition(position != null && !position.isEmpty() ? position : "默认位置");
            location.setBatchNumber(batchNumber);
            location.setStatus("正常");
            
            // 设置日期
            if (storageDateStr != null && !storageDateStr.isEmpty()) {
                location.setStorageDate(java.time.LocalDate.parse(storageDateStr));
            } else {
                location.setStorageDate(java.time.LocalDate.now());
            }
            
            if (expiryDateStr != null && !expiryDateStr.isEmpty()) {
                location.setExpiryDate(java.time.LocalDate.parse(expiryDateStr));
            }
            
            location.setCreatedAt(LocalDateTime.now());
            location.setUpdatedAt(LocalDateTime.now());
            
            // 保存库存位置
            InventoryLocation savedLocation = inventoryLocationRepository.save(location);
            
            // 更新货架层数量
            shelfLevelService.updateLevelQuantity(shelfLevelId, quantity);
            
            // 更新商品总库存
            goods.setQuantity(goods.getQuantity() + quantity);
            goods.setUpdatedAt(LocalDateTime.now());
            goodsRepository.save(goods);
            
            // 记录操作日志
            String locInfo = buildLocationInfo(savedLocation);
            String description = "新增商品入库 " + goods.getName() + " x" + quantity;
            saveOperationLog("新建入库", description, goods.getName(), quantity, locInfo);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "入库成功");
            response.put("location", InventoryLocationDTO.fromEntity(savedLocation));
            response.put("quantity", quantity);
            response.put("total", savedLocation.getQuantity());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("入库失败: " + e.getMessage());
        }
    }
}