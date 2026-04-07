package com.jie.graduationproject.repository;

import com.jie.graduationproject.model.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {

    Optional<Shelf> findByShelfCode(String shelfCode);

    //判断货架是否可用（状态为正常）
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Shelf s " +
            "WHERE s.shelfCode = :shelfCode AND s.status = '正常'")
    boolean isAvailableByShelfCode(@Param("shelfCode") String shelfCode);

    //根据区域查找所有货架（常温/冷冻）
    List<Shelf> findByArea(String area);

    //根据货架类型查找所有可用货架（高频快消区/中频干货区/低频重物区）
    List<Shelf> findByShelfTypeAndStatus(String shelfType, String status);

    //查找所有状态为正常的货架
    List<Shelf> findByStatus(String status);

    //查找某个区域内所有可用的货架（例如：常温区正常可使用区域）
    List<Shelf> findByAreaAndStatus(String area, String status);

    //统计某个类型的货架数量
    long countByShelfType(String shelfType);

    //检查货架编号是否存在
    boolean existsByShelfCode(String shelfCode);

}
