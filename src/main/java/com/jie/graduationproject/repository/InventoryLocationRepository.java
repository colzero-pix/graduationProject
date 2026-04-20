package com.jie.graduationproject.repository;

import com.jie.graduationproject.model.entity.InventoryLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryLocationRepository extends JpaRepository<InventoryLocation, Long> {

    // 根据商品ID查找所有库存位置
    List<InventoryLocation> findByGoodsId(Long goodsId);
    
    // 根据商品ID和状态查找
    List<InventoryLocation> findByGoodsIdAndStatus(Long goodsId, String status);
    
    // 根据货架层ID查找
    List<InventoryLocation> findByShelfLevelId(Long shelfLevelId);
    
    // 根据货架层ID和状态查找
    List<InventoryLocation> findByShelfLevelIdAndStatus(Long shelfLevelId, String status);
    
    // 根据批次号查找
    List<InventoryLocation> findByBatchNumber(String batchNumber);
    
    // 查找即将过期的库存
    @Query("SELECT il FROM InventoryLocation il WHERE il.expiryDate <= :date AND il.status = '正常'")
    List<InventoryLocation> findExpiringInventory(@Param("date") LocalDate date);
    
    // 查找指定天数内即将过期的库存
    @Query("SELECT il FROM InventoryLocation il WHERE il.expiryDate <= :expiryDate AND il.expiryDate > CURRENT_DATE AND il.status = '正常'")
    List<InventoryLocation> findExpiringInventoryWithinDays(@Param("expiryDate") LocalDate expiryDate);
    
    // 统计商品的库存总量
    @Query("SELECT SUM(il.quantity) FROM InventoryLocation il WHERE il.goods.id = :goodsId AND il.status = '正常'")
    Integer sumQuantityByGoodsId(@Param("goodsId") Long goodsId);
    
    // 统计货架层的库存总量
    @Query("SELECT SUM(il.quantity) FROM InventoryLocation il WHERE il.shelfLevel.id = :shelfLevelId AND il.status = '正常'")
    Integer sumQuantityByShelfLevelId(@Param("shelfLevelId") Long shelfLevelId);
    
    // 根据先进先出原则查找库存（最早入库的先出）
    @Query("SELECT il FROM InventoryLocation il WHERE il.goods.id = :goodsId AND il.status = '正常' " +
           "ORDER BY il.storageDate ASC, il.createdAt ASC")
    List<InventoryLocation> findInventoryByGoodsIdOrderByStorageDate(@Param("goodsId") Long goodsId);
    
    // 查找指定区域的库存
    @Query("SELECT il FROM InventoryLocation il WHERE il.shelfLevel.shelf.area = :area AND il.status = '正常'")
    List<InventoryLocation> findByArea(@Param("area") String area);
    
    // 查找指定货架类型的库存
    @Query("SELECT il FROM InventoryLocation il WHERE il.shelfLevel.shelf.shelfType = :shelfType AND il.status = '正常'")
    List<InventoryLocation> findByShelfType(@Param("shelfType") String shelfType);
    
    // 批量更新库存状态
    @Query("UPDATE InventoryLocation il SET il.status = :status WHERE il.id IN :ids")
    void updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") String status);
    
    // 检查商品在指定货架层是否存在
    boolean existsByGoodsIdAndShelfLevelIdAndStatus(Long goodsId, Long shelfLevelId, String status);
    
    // 查找商品在指定货架层的库存
    List<InventoryLocation> findByGoodsIdAndShelfLevelIdAndStatus(Long goodsId, Long shelfLevelId, String status);
    
    // 查找指定日期前过期的库存
    List<InventoryLocation> findByExpiryDateBeforeAndStatus(LocalDate expiryDate, String status);
}