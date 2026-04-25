package com.jie.graduationproject.service.Shelf.Impl;

import com.jie.graduationproject.model.dto.AddShelfDTO;
import com.jie.graduationproject.model.dto.ShelfQueryDTO;
import com.jie.graduationproject.model.dto.UpdateShelfDTO;
import com.jie.graduationproject.model.entity.Goods;
import com.jie.graduationproject.model.entity.Shelf;
import com.jie.graduationproject.model.entity.ShelfLevel;
import com.jie.graduationproject.model.entity.InventoryLocation;
import com.jie.graduationproject.repository.GoodsRepository;
import com.jie.graduationproject.repository.ShelfRepository;
import com.jie.graduationproject.repository.ShelfLevelRepository;
import com.jie.graduationproject.repository.InventoryLocationRepository;
import com.jie.graduationproject.service.Shelf.ShelfService;
import com.jie.graduationproject.service.ShelfLevel.ShelfLevelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class ShelfServiceImpl implements ShelfService {

    @Autowired
    private ShelfRepository shelfRepository;
    
    @Autowired
    private GoodsRepository goodsRepository;
    
    @Autowired
    private ShelfLevelService shelfLevelService;

    @Autowired
    private ShelfLevelRepository shelfLevelRepository;

    @Autowired
    private InventoryLocationRepository inventoryLocationRepository;

    @Override
    @Transactional
    public ResponseEntity<?> addShelf(AddShelfDTO addShelfDTO) {
        try {
            // 检查货架编号是否已存在
            if (shelfRepository.existsByShelfCode(addShelfDTO.getShelfCode())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("货架编号已存在：" + addShelfDTO.getShelfCode());
            }
            
            Shelf shelf = new Shelf();
            shelf.setShelfCode(addShelfDTO.getShelfCode());
            shelf.setShelfName(addShelfDTO.getShelfName());
            shelf.setArea(addShelfDTO.getArea());
            shelf.setShelfType(addShelfDTO.getShelfType());
            shelf.setLocationDesc(addShelfDTO.getLocationDesc());
            shelf.setFloor(addShelfDTO.getFloor());
            shelf.setTotalLevels(addShelfDTO.getTotalLevels());
            shelf.setLevelHeight(addShelfDTO.getLevelHeight());
            shelf.setLength(addShelfDTO.getLength());
            shelf.setDepth(addShelfDTO.getDepth());
            shelf.setMaxWeight(addShelfDTO.getMaxWeight());
            shelf.setProductTypesCount(addShelfDTO.getProductTypesCount() != null ? addShelfDTO.getProductTypesCount() : 0);
            shelf.setTotalQuantity(addShelfDTO.getTotalQuantity() != null ? addShelfDTO.getTotalQuantity() : 0);
            shelf.setIsBottomForHeavy(addShelfDTO.getIsBottomForHeavy() != null ? addShelfDTO.getIsBottomForHeavy() : false);
            shelf.setStatus(addShelfDTO.getStatus() != null ? addShelfDTO.getStatus() : "正常");
            shelf.setMainCategory(addShelfDTO.getMainCategory());
            shelf.setRemarks("");
            shelf.setCreatedAt(LocalDateTime.now());
            shelf.setUpdatedAt(LocalDateTime.now());
            
            Shelf savedShelf = shelfRepository.save(shelf);
            
            // 自动创建货架层级
            if (savedShelf.getTotalLevels() != null && savedShelf.getTotalLevels() > 0) {
                try {
                    shelfLevelService.createLevelsForShelf(savedShelf.getId(), savedShelf.getTotalLevels());
                } catch (Exception e) {
                    // 如果创建层级失败，记录错误但不影响货架创建
                    System.err.println("创建货架层级失败: " + e.getMessage());
                }
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedShelf);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加货架失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateShelf(Long id, UpdateShelfDTO updateShelfDTO) {
        try {
            Optional<Shelf> shelfOptional = shelfRepository.findById(id);
            if (shelfOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架不存在，ID：" + id);
            }
            
            Shelf shelf = shelfOptional.get();
            
            // 更新可修改的字段
            if (updateShelfDTO.getShelfName() != null) {
                shelf.setShelfName(updateShelfDTO.getShelfName());
            }
            if (updateShelfDTO.getArea() != null) {
                shelf.setArea(updateShelfDTO.getArea());
            }
            if (updateShelfDTO.getShelfType() != null) {
                shelf.setShelfType(updateShelfDTO.getShelfType());
            }
            if (updateShelfDTO.getLocationDesc() != null) {
                shelf.setLocationDesc(updateShelfDTO.getLocationDesc());
            }
            if (updateShelfDTO.getFloor() != null) {
                shelf.setFloor(updateShelfDTO.getFloor());
            }
            if (updateShelfDTO.getTotalLevels() != null) {
                shelf.setTotalLevels(updateShelfDTO.getTotalLevels());
            }
            if (updateShelfDTO.getLevelHeight() != null) {
                shelf.setLevelHeight(updateShelfDTO.getLevelHeight());
            }
            if (updateShelfDTO.getLength() != null) {
                shelf.setLength(updateShelfDTO.getLength());
            }
            if (updateShelfDTO.getDepth() != null) {
                shelf.setDepth(updateShelfDTO.getDepth());
            }
            if (updateShelfDTO.getMaxWeight() != null) {
                shelf.setMaxWeight(updateShelfDTO.getMaxWeight());
            }
            if (updateShelfDTO.getProductTypesCount() != null) {
                shelf.setProductTypesCount(updateShelfDTO.getProductTypesCount());
            }
            if (updateShelfDTO.getTotalQuantity() != null) {
                shelf.setTotalQuantity(updateShelfDTO.getTotalQuantity());
            }
            if (updateShelfDTO.getIsBottomForHeavy() != null) {
                shelf.setIsBottomForHeavy(updateShelfDTO.getIsBottomForHeavy());
            }
            if (updateShelfDTO.getStatus() != null) {
                shelf.setStatus(updateShelfDTO.getStatus());
            }
            if (updateShelfDTO.getMainCategory() != null) {
                shelf.setMainCategory(updateShelfDTO.getMainCategory());
            }
            if (updateShelfDTO.getRemarks() != null) {
                shelf.setRemarks(updateShelfDTO.getRemarks());
            }
            
            shelf.setUpdatedAt(LocalDateTime.now());
            Shelf updatedShelf = shelfRepository.save(shelf);
            return ResponseEntity.ok(updatedShelf);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新货架失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteShelf(Long id) {
        try {
            if (!shelfRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架不存在，ID：" + id);
            }
            
            // 检查货架是否还有商品（通过货架编号查找）
            Shelf shelf = shelfRepository.findById(id).orElse(null);
            if (shelf != null) {
                // 查找位置包含货架编号的商品
                List<Goods> goodsOnShelf = goodsRepository.findByLocationContaining(shelf.getShelfCode());
                if (!goodsOnShelf.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("货架上还有商品，无法删除。请先移走所有商品。");
                }
            }
            
            shelfRepository.deleteById(id);
            return ResponseEntity.ok("货架删除成功");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除货架失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getShelfById(Long id) {
        try {
            Optional<Shelf> shelfOptional = shelfRepository.findById(id);
            if (shelfOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架不存在，ID：" + id);
            }

            Shelf shelf = shelfOptional.get();
            updateShelfStatistics(shelf);
            
            return ResponseEntity.ok(shelf);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("查询货架失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAllShelves() {
        try {
            List<Shelf> shelves = shelfRepository.findAll();
            shelves.forEach(this::updateShelfStatistics);
            return ResponseEntity.ok(shelves);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("查询货架列表失败：" + e.getMessage());
        }
    }

    private void updateShelfStatistics(Shelf shelf) {
        List<ShelfLevel> levels = shelfLevelRepository.findByShelfId(shelf.getId());
        
        int totalQty = 0;
        Set<Long> goodsIds = new HashSet<>();
        
        for (ShelfLevel level : levels) {
            List<InventoryLocation> locations = inventoryLocationRepository.findByShelfLevelId(level.getId());
            for (InventoryLocation loc : locations) {
                totalQty += loc.getQuantity() != null ? loc.getQuantity() : 0;
                if (loc.getGoods() != null) {
                    goodsIds.add(loc.getGoods().getId());
                }
            }
        }
        
        shelf.setTotalQuantity(totalQty);
        shelf.setProductTypesCount(goodsIds.size());
    }

    @Override
    public ResponseEntity<?> searchShelves(ShelfQueryDTO queryDTO) {
        try {
            List<Shelf> shelves = shelfRepository.findAll();
            
            // 简单过滤（实际项目中应该使用JPA Specification或QueryDSL）
            List<Shelf> filteredList = shelves.stream()
                    .filter(shelf -> {
                        boolean match = true;
                        
                        if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().isEmpty()) {
                            match = match && (shelf.getShelfCode().contains(queryDTO.getKeyword()) ||
                                           (shelf.getShelfName() != null && shelf.getShelfName().contains(queryDTO.getKeyword())) ||
                                           shelf.getArea().contains(queryDTO.getKeyword()));
                        }
                        
                        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
                            match = match && shelf.getStatus().equals(queryDTO.getStatus());
                        }
                        
                        if (queryDTO.getArea() != null && !queryDTO.getArea().isEmpty()) {
                            match = match && shelf.getArea().equals(queryDTO.getArea());
                        }
                        
                        if (queryDTO.getShelfType() != null && !queryDTO.getShelfType().isEmpty()) {
                            match = match && shelf.getShelfType().equals(queryDTO.getShelfType());
                        }
                        
                        if (queryDTO.getMainCategory() != null && !queryDTO.getMainCategory().isEmpty()) {
                            match = match && shelf.getMainCategory() != null && 
                                    shelf.getMainCategory().contains(queryDTO.getMainCategory());
                        }
                        
                        if (queryDTO.getAvailableOnly() != null && queryDTO.getAvailableOnly()) {
                            match = match && "正常".equals(shelf.getStatus()) && 
                                    (shelf.getTotalQuantity() == null || shelf.getTotalQuantity() < 100); // 假设阈值是100
                        }
                        
                        if (queryDTO.getHighUtilization() != null && queryDTO.getHighUtilization()) {
                            // 简单计算使用率：假设每个货架最大容量为1000
                            double utilization = (shelf.getTotalQuantity() != null ? shelf.getTotalQuantity() : 0) / 1000.0;
                            match = match && utilization > 0.8;
                        }
                        
                        if (queryDTO.getEmptyOnly() != null && queryDTO.getEmptyOnly()) {
                            match = match && (shelf.getTotalQuantity() == null || shelf.getTotalQuantity() == 0);
                        }
                        
                        if (queryDTO.getFloor() != null) {
                            match = match && shelf.getFloor() != null && 
                                    shelf.getFloor().equals(queryDTO.getFloor());
                        }
                        
                        if (queryDTO.getMinWeightCapacity() != null) {
                            match = match && shelf.getMaxWeight() != null && 
                                    shelf.getMaxWeight() >= queryDTO.getMinWeightCapacity();
                        }
                        
                        return match;
                    })
                    .toList();
            
            // 简单排序
            if (queryDTO.getSortBy() != null && !queryDTO.getSortBy().isEmpty()) {
                filteredList.sort((s1, s2) -> {
                    int result = 0;
                    String direction = queryDTO.getSortDirection() != null ? 
                            queryDTO.getSortDirection().toLowerCase() : "asc";
                    
                    switch (queryDTO.getSortBy().toLowerCase()) {
                        case "shelfcode":
                            result = s1.getShelfCode().compareTo(s2.getShelfCode());
                            break;
                        case "totalquantity":
                            Integer q1 = s1.getTotalQuantity() != null ? s1.getTotalQuantity() : 0;
                            Integer q2 = s2.getTotalQuantity() != null ? s2.getTotalQuantity() : 0;
                            result = q1.compareTo(q2);
                            break;
                        case "createdat":
                            result = s1.getCreatedAt().compareTo(s2.getCreatedAt());
                            break;
                        default:
                            result = s1.getShelfCode().compareTo(s2.getShelfCode());
                    }
                    
                    return "desc".equals(direction) ? -result : result;
                });
            }
            
            return ResponseEntity.ok(filteredList);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("搜索货架失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> assignGoodsToShelf(Long shelfId, Long goodsId, String location) {
        try {
            Optional<Shelf> shelfOptional = shelfRepository.findById(shelfId);
            if (shelfOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架不存在，ID：" + shelfId);
            }
            
            Optional<Goods> goodsOptional = goodsRepository.findById(goodsId);
            if (goodsOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("商品不存在，ID：" + goodsId);
            }
            
            Shelf shelf = shelfOptional.get();
            Goods goods = goodsOptional.get();
            
            // 检查货架状态
            if (!"正常".equals(shelf.getStatus())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("货架状态异常，无法分配商品。当前状态：" + shelf.getStatus());
            }
            
            // 检查货架承重（简单检查：假设每件商品平均0.5kg）
            if (shelf.getMaxWeight() != null) {
                // 简单检查：假设货架当前承重为 totalQuantity * 平均重量
                double currentWeight = shelf.getTotalQuantity() != null ? shelf.getTotalQuantity() * 0.5 : 0; // 假设平均0.5kg/件
                double addedWeight = goods.getQuantity() != null ? goods.getQuantity() * 0.5 : 0;
                double newWeight = currentWeight + addedWeight;
                if (newWeight > shelf.getMaxWeight()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("货架承重不足。最大承重：" + shelf.getMaxWeight() + "kg，预计总重：" + newWeight + "kg");
                }
            }
            
            // 更新商品信息
            // 位置格式：货架编号-层数，如：A-01-3
            String newLocation = shelf.getShelfCode() + "-" + location;
            goods.setLocation(newLocation);
            goods.setUpdatedAt(LocalDateTime.now());
            goodsRepository.save(goods);
            
            // 更新货架统计信息
            shelf.setProductTypesCount(shelf.getProductTypesCount() != null ? shelf.getProductTypesCount() + 1 : 1);
            shelf.setTotalQuantity(shelf.getTotalQuantity() != null ? shelf.getTotalQuantity() + goods.getQuantity() : goods.getQuantity());
            shelf.setUpdatedAt(LocalDateTime.now());
            shelfRepository.save(shelf);
            
            return ResponseEntity.ok("商品分配成功");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("分配商品到货架失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getShelfUtilization(Long id) {
        try {
            Optional<Shelf> shelfOptional = shelfRepository.findById(id);
            if (shelfOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架不存在，ID：" + id);
            }
            
            Shelf shelf = shelfOptional.get();
            
            // 计算使用率（简单示例）
            // 实际项目中应该有更复杂的计算逻辑
            double utilizationRate = 0.0;
            String utilizationLevel = "低";
            
            if (shelf.getTotalQuantity() != null && shelf.getTotalLevels() != null) {
                // 假设每层最多存放100件商品
                int maxCapacity = shelf.getTotalLevels() * 100;
                utilizationRate = (double) shelf.getTotalQuantity() / maxCapacity * 100;
                
                if (utilizationRate >= 80) {
                    utilizationLevel = "高";
                } else if (utilizationRate >= 50) {
                    utilizationLevel = "中";
                }
            }
            
            // 返回使用率信息
            String result = String.format("货架使用率：%.1f%% (%s)", utilizationRate, utilizationLevel);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取货架使用率失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAvailableShelves(String area, String shelfType) {
        try {
            List<Shelf> allShelves = shelfRepository.findAll();
            
            List<Shelf> availableShelves = allShelves.stream()
                    .filter(shelf -> {
                        boolean match = "正常".equals(shelf.getStatus());
                        
                        if (area != null && !area.isEmpty()) {
                            match = match && area.equals(shelf.getArea());
                        }
                        
                        if (shelfType != null && !shelfType.isEmpty()) {
                            match = match && shelfType.equals(shelf.getShelfType());
                        }
                        
                        // 可用货架：使用率低于80%
                        if (shelf.getTotalQuantity() != null && shelf.getTotalLevels() != null) {
                            int maxCapacity = shelf.getTotalLevels() * 100;
                            double utilizationRate = (double) shelf.getTotalQuantity() / maxCapacity * 100;
                            match = match && utilizationRate < 80;
                        }
                        
                        return match;
                    })
                    .toList();
            
            return ResponseEntity.ok(availableShelves);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取可用货架失败：" + e.getMessage());
        }
    }
}