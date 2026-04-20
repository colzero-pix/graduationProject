package com.jie.graduationproject.repository;

import com.jie.graduationproject.model.entity.Shelf;
import com.jie.graduationproject.model.entity.ShelfLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShelfLevelRepository extends JpaRepository<ShelfLevel, Long> {

    // 根据货架ID查找所有层
    List<ShelfLevel> findByShelfId(Long shelfId);
    
    // 根据货架ID和层数查找
    Optional<ShelfLevel> findByShelfIdAndLevelNumber(Long shelfId, Integer levelNumber);
    
    // 根据货架ID和状态查找
    List<ShelfLevel> findByShelfIdAndStatus(Long shelfId, String status);
    
    // 查找有剩余容量的货架层
    @Query("SELECT sl FROM ShelfLevel sl WHERE sl.capacity > sl.currentQuantity AND sl.status != '禁用'")
    List<ShelfLevel> findAvailableLevels();
    
    // 根据区域查找可用货架层
    @Query("SELECT sl FROM ShelfLevel sl WHERE sl.capacity > sl.currentQuantity " +
           "AND sl.shelf.area = :area AND sl.status != '禁用'")
    List<ShelfLevel> findAvailableLevelsByArea(@Param("area") String area);
    
    // 根据货架类型查找可用货架层
    @Query("SELECT sl FROM ShelfLevel sl WHERE sl.capacity > sl.currentQuantity " +
           "AND sl.shelf.shelfType = :shelfType AND sl.status != '禁用'")
    List<ShelfLevel> findAvailableLevelsByShelfType(@Param("shelfType") String shelfType);
    
    // 统计货架层的使用情况
    @Query("SELECT sl.shelf.area, COUNT(sl) as total, " +
           "SUM(CASE WHEN sl.currentQuantity = 0 THEN 1 ELSE 0 END) as empty, " +
           "SUM(CASE WHEN sl.currentQuantity > 0 AND sl.currentQuantity < sl.capacity THEN 1 ELSE 0 END) as partial, " +
           "SUM(CASE WHEN sl.currentQuantity >= sl.capacity THEN 1 ELSE 0 END) as full " +
           "FROM ShelfLevel sl GROUP BY sl.shelf.area")
    List<Object[]> getLevelUsageByArea();
    
    // 更新货架层当前数量
    @Query("UPDATE ShelfLevel sl SET sl.currentQuantity = sl.currentQuantity + :quantityChange WHERE sl.id = :levelId")
    void updateCurrentQuantity(@Param("levelId") Long levelId, @Param("quantityChange") Integer quantityChange);
}