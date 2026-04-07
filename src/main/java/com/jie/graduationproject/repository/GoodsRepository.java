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
}
