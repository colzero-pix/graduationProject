package com.jie.graduationproject.service.Goods;

import com.jie.graduationproject.model.dto.AddGoodsDTO;
import com.jie.graduationproject.model.dto.GoodsQueryDTO;
import com.jie.graduationproject.model.dto.UpdateGoodsDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface GoodsService {

    public ResponseEntity<?> addGoods(AddGoodsDTO addGoodsDTO);

    public ResponseEntity<?> updateGoods(Long id, UpdateGoodsDTO updateGoodsDTO);

    public ResponseEntity<?> deleteGoods(Long id);

    public ResponseEntity<?> getGoodsById(Long id);

    public ResponseEntity<?> getAllGoods();

    public ResponseEntity<?> searchGoods(GoodsQueryDTO queryDTO);

    public ResponseEntity<?> updateStock(Long id, Integer quantityChange, String operationType);

    public ResponseEntity<?> getLowStockGoods();

    // 查询商品详情（包含库存位置信息）
    public ResponseEntity<?> getGoodsDetail(Long goodsId);

}
