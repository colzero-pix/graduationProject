package com.jie.graduationproject.service.Goods;

import com.jie.graduationproject.model.dto.AddGoodsDTO;
import com.jie.graduationproject.model.dto.GoodsQueryDTO;
import com.jie.graduationproject.model.dto.UpdateGoodsDTO;
import com.jie.graduationproject.model.entity.Goods;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GoodsService {

    public ResponseEntity<?> addGoods(AddGoodsDTO addGoodsDTO);

    public ResponseEntity<?> updateGoods(Long id, UpdateGoodsDTO updateGoodsDTO);

    public ResponseEntity<?> deleteGoods(Long id);

    public ResponseEntity<?> getGoodsById(Long id);

    public ResponseEntity<?> getAllGoods();

    public ResponseEntity<?> searchGoods(GoodsQueryDTO queryDTO);

    public ResponseEntity<?> updateStock(Long id, Integer quantityChange, String operationType);

    public List<Goods> getGoodsByExpiryDate();

    public ResponseEntity<?> getLowStockGoods();

    public ResponseEntity<?> getExpiringGoods(Integer days);

}
