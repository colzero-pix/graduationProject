package com.jie.graduationproject.service.ShelfLevel;

import com.jie.graduationproject.model.dto.ShelfLevelDTO;
import com.jie.graduationproject.model.entity.ShelfLevel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ShelfLevelService {

    // 获取货架的所有层
    ResponseEntity<?> getShelfLevels(Long shelfId);
    
    // 获取货架层详情
    ResponseEntity<?> getShelfLevelById(Long levelId);
    
    // 创建货架层
    ResponseEntity<?> createShelfLevel(ShelfLevel shelfLevel);
    
    // 批量创建货架层（根据货架总层数自动创建）
    ResponseEntity<?> createLevelsForShelf(Long shelfId, Integer totalLevels);
    
    // 更新货架层
    ResponseEntity<?> updateShelfLevel(Long levelId, ShelfLevel shelfLevel);
    
    // 删除货架层
    ResponseEntity<?> deleteShelfLevel(Long levelId);
    
    // 获取可用货架层（有剩余容量的）
    ResponseEntity<?> getAvailableShelfLevels(Long shelfId, Integer minCapacity);
    
    // 根据区域获取可用货架层
    ResponseEntity<?> getAvailableLevelsByArea(String area);
    
    // 根据货架类型获取可用货架层
    ResponseEntity<?> getAvailableLevelsByShelfType(String shelfType);
    
    // 获取货架层使用统计
    ResponseEntity<?> getShelfLevelStatistics(Long levelId);
    
    // 更新货架层当前数量
    ResponseEntity<?> updateLevelQuantity(Long levelId, Integer quantityChange);
    
    // 检查货架层容量
    boolean checkLevelCapacity(Long levelId, Integer requiredQuantity);
    
    // 获取推荐存放位置
    List<ShelfLevelDTO> getRecommendedLevels(String area, String shelfType, Integer requiredQuantity);
    
    // 获取适合商品的货架层
    ResponseEntity<?> getSuitableLevelsForGoods(Long goodsId, String storageTemperature);
}