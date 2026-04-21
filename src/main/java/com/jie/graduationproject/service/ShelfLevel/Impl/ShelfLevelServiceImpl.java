package com.jie.graduationproject.service.ShelfLevel.Impl;

import com.jie.graduationproject.model.dto.ShelfLevelDTO;
import com.jie.graduationproject.model.entity.Shelf;
import com.jie.graduationproject.model.entity.ShelfLevel;
import com.jie.graduationproject.model.entity.InventoryLocation;
import com.jie.graduationproject.repository.ShelfLevelRepository;
import com.jie.graduationproject.repository.ShelfRepository;
import com.jie.graduationproject.repository.InventoryLocationRepository;
import com.jie.graduationproject.service.ShelfLevel.ShelfLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShelfLevelServiceImpl implements ShelfLevelService {

    private final ShelfLevelRepository shelfLevelRepository;
    private final ShelfRepository shelfRepository;
    private final InventoryLocationRepository inventoryLocationRepository;

    @Autowired
    public ShelfLevelServiceImpl(ShelfLevelRepository shelfLevelRepository, 
                                 ShelfRepository shelfRepository,
                                 InventoryLocationRepository inventoryLocationRepository) {
        this.shelfLevelRepository = shelfLevelRepository;
        this.shelfRepository = shelfRepository;
        this.inventoryLocationRepository = inventoryLocationRepository;
    }

    @Override
    public ResponseEntity<?> getShelfLevels(Long shelfId) {
        try {
            List<ShelfLevel> levels = shelfLevelRepository.findByShelfId(shelfId);
            List<ShelfLevelDTO> levelDTOs = levels.stream()
                    .map(ShelfLevelDTO::fromEntity)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(levelDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取货架层失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getShelfLevelById(Long levelId) {
        try {
            Optional<ShelfLevel> optionalLevel = shelfLevelRepository.findById(levelId);
            if (optionalLevel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架层不存在");
            }
            
            ShelfLevel level = optionalLevel.get();
            ShelfLevelDTO levelDTO = ShelfLevelDTO.fromEntity(level);
            
            // 可以在这里添加该层的商品信息
            // levelDTO.setGoodsList(getGoodsInLevel(levelId));
            
            return ResponseEntity.ok(levelDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取货架层详情失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> createShelfLevel(ShelfLevel shelfLevel) {
        try {
            // 验证货架是否存在
            Optional<Shelf> optionalShelf = shelfRepository.findById(shelfLevel.getShelf().getId());
            if (optionalShelf.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架不存在");
            }
            
            Shelf shelf = optionalShelf.get();
            
            // 检查层数是否重复
            Optional<ShelfLevel> existingLevel = shelfLevelRepository.findByShelfIdAndLevelNumber(
                    shelf.getId(), shelfLevel.getLevelNumber());
            if (existingLevel.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("该层数已存在");
            }
            
            // 设置默认值
            shelfLevel.setShelf(shelf);
            if (shelfLevel.getCurrentQuantity() == null) {
                shelfLevel.setCurrentQuantity(0);
            }
            if (shelfLevel.getStatus() == null) {
                shelfLevel.setStatus("空闲");
            }
            shelfLevel.setCreatedAt(LocalDateTime.now());
            shelfLevel.setUpdatedAt(LocalDateTime.now());
            
            // 更新状态
            shelfLevel.updateStatus();
            
            ShelfLevel savedLevel = shelfLevelRepository.save(shelfLevel);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ShelfLevelDTO.fromEntity(savedLevel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("创建货架层失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> createLevelsForShelf(Long shelfId, Integer totalLevels) {
        try {
            Optional<Shelf> optionalShelf = shelfRepository.findById(shelfId);
            if (optionalShelf.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架不存在");
            }
            
            Shelf shelf = optionalShelf.get();
            
            // 删除现有的层（如果需要重新创建）
            List<ShelfLevel> existingLevels = shelfLevelRepository.findByShelfId(shelfId);
            if (!existingLevels.isEmpty()) {
                shelfLevelRepository.deleteAll(existingLevels);
            }
            
            // 创建新的层
            List<ShelfLevel> newLevels = new ArrayList<>();
            for (int i = 1; i <= totalLevels; i++) {
                ShelfLevel level = new ShelfLevel();
                level.setShelf(shelf);
                level.setLevelNumber(i);
                level.setLevelName("第" + i + "层");
                level.setCapacity(100); // 默认容量
                level.setCurrentQuantity(0);
                level.setStatus("空闲");
                level.setCreatedAt(LocalDateTime.now());
                level.setUpdatedAt(LocalDateTime.now());
                
                newLevels.add(level);
            }
            
            List<ShelfLevel> savedLevels = shelfLevelRepository.saveAll(newLevels);
            List<ShelfLevelDTO> levelDTOs = savedLevels.stream()
                    .map(ShelfLevelDTO::fromEntity)
                    .collect(Collectors.toList());
            
            // 更新货架的总层数
            shelf.setTotalLevels(totalLevels);
            shelf.setUpdatedAt(LocalDateTime.now());
            shelfRepository.save(shelf);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(levelDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("批量创建货架层失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateShelfLevel(Long levelId, ShelfLevel shelfLevel) {
        try {
            Optional<ShelfLevel> optionalLevel = shelfLevelRepository.findById(levelId);
            if (optionalLevel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架层不存在");
            }
            
            ShelfLevel existingLevel = optionalLevel.get();
            
            // 更新字段
            if (shelfLevel.getLevelName() != null) {
                existingLevel.setLevelName(shelfLevel.getLevelName());
            }
            if (shelfLevel.getCapacity() != null) {
                existingLevel.setCapacity(shelfLevel.getCapacity());
            }
            if (shelfLevel.getRemarks() != null) {
                existingLevel.setRemarks(shelfLevel.getRemarks());
            }
            if (shelfLevel.getStatus() != null) {
                existingLevel.setStatus(shelfLevel.getStatus());
            }
            
            existingLevel.setUpdatedAt(LocalDateTime.now());
            existingLevel.updateStatus();
            
            ShelfLevel updatedLevel = shelfLevelRepository.save(existingLevel);
            return ResponseEntity.ok(ShelfLevelDTO.fromEntity(updatedLevel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新货架层失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteShelfLevel(Long levelId) {
        try {
            Optional<ShelfLevel> optionalLevel = shelfLevelRepository.findById(levelId);
            if (optionalLevel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架层不存在");
            }
            
            ShelfLevel level = optionalLevel.get();
            
            // 检查是否有库存
            if (level.getCurrentQuantity() > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("货架层还有库存，无法删除");
            }
            
            shelfLevelRepository.delete(level);
            return ResponseEntity.ok("货架层删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除货架层失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAvailableShelfLevels(Long shelfId, Integer minCapacity) {
        try {
            List<ShelfLevel> availableLevels;
            
            if (shelfId != null) {
                // 获取指定货架的可用层
                availableLevels = shelfLevelRepository.findByShelfId(shelfId).stream()
                        .filter(level -> level.getRemainingCapacity() > 0)
                        .collect(Collectors.toList());
            } else {
                // 获取所有可用层
                availableLevels = shelfLevelRepository.findAvailableLevels();
            }
            
            // 过滤最小容量要求
            if (minCapacity != null && minCapacity > 0) {
                availableLevels = availableLevels.stream()
                        .filter(level -> level.getRemainingCapacity() >= minCapacity)
                        .collect(Collectors.toList());
            }
            
            List<ShelfLevelDTO> levelDTOs = availableLevels.stream()
                    .map(ShelfLevelDTO::fromEntity)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(levelDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取可用货架层失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAvailableLevelsByArea(String area) {
        try {
            List<ShelfLevel> availableLevels = shelfLevelRepository.findAvailableLevelsByArea(area);
            List<ShelfLevelDTO> levelDTOs = availableLevels.stream()
                    .map(ShelfLevelDTO::fromEntity)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(levelDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取区域可用货架层失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAvailableLevelsByShelfType(String shelfType) {
        try {
            List<ShelfLevel> availableLevels = shelfLevelRepository.findAvailableLevelsByShelfType(shelfType);
            List<ShelfLevelDTO> levelDTOs = availableLevels.stream()
                    .map(ShelfLevelDTO::fromEntity)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(levelDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取货架类型可用货架层失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getShelfLevelStatistics(Long levelId) {
        try {
            // 检查货架层是否存在
            Optional<ShelfLevel> optionalLevel = shelfLevelRepository.findById(levelId);
            if (optionalLevel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架层不存在");
            }
            
            ShelfLevel level = optionalLevel.get();
            
            // 获取该层的库存信息
            int capacity = level.getCapacity() != null ? level.getCapacity() : 0;
            int currentQuantity = level.getCurrentQuantity();
            int remainingCapacity = capacity - currentQuantity;
            double usageRate = capacity > 0 ? (double) currentQuantity / capacity * 100 : 0;
            
            // 获取该层存放的商品信息
            // 这里需要调用InventoryLocationService来获取该层的库存详情
            // 暂时返回基本信息
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("levelId", levelId);
            statistics.put("levelName", level.getLevelName());
            statistics.put("capacity", capacity);
            statistics.put("currentQuantity", currentQuantity);
            statistics.put("remainingCapacity", remainingCapacity);
            statistics.put("usageRate", String.format("%.2f%%", usageRate));
            statistics.put("status", level.getStatus());
            statistics.put("shelfName", level.getShelf().getShelfName());
            statistics.put("area", level.getShelf().getArea());
            statistics.put("shelfType", level.getShelf().getShelfType());
            
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取货架层统计失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateLevelQuantity(Long levelId, Integer quantityChange) {
        try {
            Optional<ShelfLevel> optionalLevel = shelfLevelRepository.findById(levelId);
            if (optionalLevel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("货架层不存在");
            }
            
            ShelfLevel level = optionalLevel.get();
            
            // 计算新的数量
            int newQuantity = level.getCurrentQuantity() + quantityChange;
            
            // 验证容量
            if (newQuantity < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("库存数量不能为负数");
            }
            
            if (level.getCapacity() != null && newQuantity > level.getCapacity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("超出货架层容量");
            }
            
            // 更新数量
            level.setCurrentQuantity(newQuantity);
            level.updateStatus();
            level.setUpdatedAt(LocalDateTime.now());
            
            ShelfLevel updatedLevel = shelfLevelRepository.save(level);
            return ResponseEntity.ok(ShelfLevelDTO.fromEntity(updatedLevel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新货架层数量失败: " + e.getMessage());
        }
    }

    @Override
    public boolean checkLevelCapacity(Long levelId, Integer requiredQuantity) {
        try {
            Optional<ShelfLevel> optionalLevel = shelfLevelRepository.findById(levelId);
            if (optionalLevel.isEmpty()) {
                return false;
            }
            
            ShelfLevel level = optionalLevel.get();
            if (level.getCapacity() == null) {
                return false;
            }
            
            int availableCapacity = level.getCapacity() - level.getCurrentQuantity();
            return availableCapacity >= requiredQuantity;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<ShelfLevelDTO> getRecommendedLevels(String area, String shelfType, Integer requiredQuantity) {
        try {
            List<ShelfLevel> availableLevels;
            
            if (area != null && shelfType != null) {
                // 同时指定区域和货架类型
                availableLevels = shelfLevelRepository.findAvailableLevelsByArea(area).stream()
                        .filter(level -> level.getShelf().getShelfType().equals(shelfType))
                        .collect(Collectors.toList());
            } else if (area != null) {
                availableLevels = shelfLevelRepository.findAvailableLevelsByArea(area);
            } else if (shelfType != null) {
                availableLevels = shelfLevelRepository.findAvailableLevelsByShelfType(shelfType);
            } else {
                availableLevels = shelfLevelRepository.findAvailableLevels();
            }
            
            // 按剩余容量排序（从大到小）
            availableLevels.sort((a, b) -> {
                int remainingA = a.getRemainingCapacity();
                int remainingB = b.getRemainingCapacity();
                return Integer.compare(remainingB, remainingA);
            });
            
            // 过滤出容量足够的货架层
            List<ShelfLevel> suitableLevels = availableLevels.stream()
                    .filter(level -> level.getRemainingCapacity() >= requiredQuantity)
                    .collect(Collectors.toList());
            
            return suitableLevels.stream()
                    .map(ShelfLevelDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public ResponseEntity<?> getSuitableLevelsForGoods(Long goodsId, String storageTemperature) {
        try {
            // 1. 根据存储温度要求筛选合适的货架区域
            // 存储温度与货架区域的映射关系
            Map<String, List<String>> temperatureToAreaMap = new HashMap<>();
            temperatureToAreaMap.put("常温", Arrays.asList("常温区", "干货区"));
            temperatureToAreaMap.put("冷藏", Arrays.asList("冷藏区"));
            temperatureToAreaMap.put("冷冻", Arrays.asList("冷冻区"));
            
            List<String> suitableAreas = temperatureToAreaMap.getOrDefault(storageTemperature, 
                    Arrays.asList("常温区", "干货区")); // 默认值
            
            // 2. 查询符合条件的货架层
            List<ShelfLevel> allLevels = shelfLevelRepository.findAll();
            
            // 3. 筛选合适的货架层
            List<Map<String, Object>> suitableLevels = new ArrayList<>();
            
            for (ShelfLevel level : allLevels) {
                Shelf shelf = level.getShelf();
                
                // 检查区域是否合适
                if (!suitableAreas.contains(shelf.getArea())) {
                    continue;
                }
                
                // 检查货架状态
                if (!"正常".equals(shelf.getStatus())) {
                    continue;
                }
                
                // 检查货架层状态
                if (!"空闲".equals(level.getStatus()) && !"部分占用".equals(level.getStatus())) {
                    continue;
                }
                
                // 计算剩余容量
                int remainingCapacity = level.getRemainingCapacity();
                if (remainingCapacity <= 0) {
                    continue;
                }
                
                // 检查该货架层是否已经存储了相同的商品
                boolean hasSameGoods = false;
                if (goodsId != null) {
                    hasSameGoods = inventoryLocationRepository.existsByGoodsIdAndShelfLevelIdAndStatus(goodsId, level.getId(), "正常");
                }
                
                // 创建返回对象
                Map<String, Object> levelInfo = new HashMap<>();
                levelInfo.put("id", level.getId());
                levelInfo.put("shelfId", shelf.getId());
                levelInfo.put("shelfCode", shelf.getShelfCode());
                levelInfo.put("shelfName", shelf.getShelfName());
                levelInfo.put("area", shelf.getArea());
                levelInfo.put("shelfType", shelf.getShelfType());
                levelInfo.put("levelId", level.getId());
                levelInfo.put("levelName", level.getLevelName());
                levelInfo.put("levelNumber", level.getLevelNumber());
                levelInfo.put("capacity", level.getCapacity());
                levelInfo.put("currentQuantity", level.getCurrentQuantity());
                levelInfo.put("remainingCapacity", remainingCapacity);
                levelInfo.put("hasSameGoods", hasSameGoods);
                levelInfo.put("status", level.getStatus());
                
                suitableLevels.add(levelInfo);
            }
            
            // 4. 排序：先按是否有相同商品排序，再按剩余容量排序
            suitableLevels.sort((a, b) -> {
                boolean aHasSameGoods = (boolean) a.get("hasSameGoods");
                boolean bHasSameGoods = (boolean) b.get("hasSameGoods");
                
                // 优先显示有相同商品的位置
                if (aHasSameGoods && !bHasSameGoods) {
                    return -1;
                } else if (!aHasSameGoods && bHasSameGoods) {
                    return 1;
                } else {
                    // 都有或都没有相同商品，按剩余容量从大到小排序
                    int aRemaining = (int) a.get("remainingCapacity");
                    int bRemaining = (int) b.get("remainingCapacity");
                    return Integer.compare(bRemaining, aRemaining);
                }
            });
            
            return ResponseEntity.ok(suitableLevels);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("查询合适货架层失败: " + e.getMessage());
        }
    }
}