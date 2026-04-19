package com.jie.graduationproject.repository;

import com.jie.graduationproject.model.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    Goods findGoodsById(Long id);

    void deleteGoodsByName(String name);

    Optional<Goods> findGoodsByName(String name);

    boolean existsGoodsById(Long id);

    boolean existsGoodsByName(String name);

    List<Goods> findAllByOrderByExpiryDateAsc();
    
    // 根据位置查找商品（位置格式如：A-01-3 表示A区01号货架第3层）
    List<Goods> findByLocationContaining(String location);
    
    // 根据SKU查找商品
    Optional<Goods> findBySku(String sku);
}
