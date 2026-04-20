package com.jie.graduationproject.service.InventoryLocation;

import com.jie.graduationproject.model.dto.AddGoodsWithLocationDTO;
import com.jie.graduationproject.model.dto.InventoryLocationDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface InventoryLocationService {

    // 添加商品并指定存放位置
    ResponseEntity<?> addGoodsWithLocation(AddGoodsWithLocationDTO request);
    
    // 获取商品的所有库存位置
    ResponseEntity<?> getGoodsLocations(Long goodsId);
    
    // 获取货架层的所有库存
    ResponseEntity<?> getLevelInventory(Long shelfLevelId);
    
    // 移动库存（从一个位置到另一个位置）
    ResponseEntity<?> moveInventory(Long fromLocationId, Long toShelfLevelId, Integer quantity, String position);
    
    // 出库操作
    ResponseEntity<?> outboundInventory(Long locationId, Integer quantity);
    
    // 入库操作（添加到现有库存位置）
    ResponseEntity<?> inboundInventory(Long locationId, Integer quantity);
    
    // 更新库存位置信息
    ResponseEntity<?> updateInventoryLocation(Long locationId, InventoryLocationDTO locationDTO);
    
    // 删除库存位置
    ResponseEntity<?> deleteInventoryLocation(Long locationId);
    
    // 获取即将过期的库存
    ResponseEntity<?> getExpiringInventory(Integer days);
    
    // 根据先进先出原则获取出库建议
    ResponseEntity<?> getFIFOOutboundSuggestions(Long goodsId, Integer requiredQuantity);
    
    // 获取库存统计
    ResponseEntity<?> getInventoryStatistics();
    
    // 搜索库存
    ResponseEntity<?> searchInventory(String keyword, String area, String shelfType, LocalDate expiryDate);
    
    // 批量更新库存状态
    ResponseEntity<?> batchUpdateStatus(List<Long> locationIds, String status);
}