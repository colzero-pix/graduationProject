package com.jie.graduationproject.service.Goods.Impl;

import com.jie.graduationproject.model.dto.AddGoodsDTO;
import com.jie.graduationproject.model.dto.GoodsQueryDTO;
import com.jie.graduationproject.model.dto.UpdateGoodsDTO;
import com.jie.graduationproject.model.entity.Goods;
import com.jie.graduationproject.repository.GoodsRepository;
import com.jie.graduationproject.service.Goods.GoodsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<?> addGoods(@Valid AddGoodsDTO addGoodsDTO) {
        try {
            // 检查商品是否已存在
            if (goodsRepository.existsGoodsByName(addGoodsDTO.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("商品名称已存在");
            }

            Goods goods = new Goods();
            goods.setSku(addGoodsDTO.getSku());
            goods.setName(addGoodsDTO.getName());
            goods.setStorageTemperature(addGoodsDTO.getStorageTemperature());
            goods.setLocation(addGoodsDTO.getLocation());
            goods.setStatus(addGoodsDTO.getStatus() != null ? addGoodsDTO.getStatus() : "正常");
            goods.setQuantity(addGoodsDTO.getGoodsQuantity() != null ? addGoodsDTO.getGoodsQuantity() : 0);
            goods.setThreshold(addGoodsDTO.getGoodsQuantity() != null ? addGoodsDTO.getGoodsQuantity() / 5 : 0); // 默认阈值为库存的20%
            goods.setStorageDate(addGoodsDTO.getStorageDate() != null ? addGoodsDTO.getStorageDate() : LocalDate.now());
            goods.setExpiryDate(addGoodsDTO.getExpiryDate());
            goods.setSupplierName(addGoodsDTO.getSupplierName());
            goods.setSupplierContact(addGoodsDTO.getSupplierContact());
            goods.setCreatedAt(LocalDateTime.now());
            goods.setUpdatedAt(LocalDateTime.now());

            goodsRepository.save(goods);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(goods);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("商品信息添加失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateGoods(Long id, UpdateGoodsDTO updateGoodsDTO) {
        try {
            Optional<Goods> optionalGoods = goodsRepository.findById(id);
            if (optionalGoods.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("商品不存在");
            }

            Goods goods = optionalGoods.get();
            goods.setName(updateGoodsDTO.getName());
            goods.setStorageTemperature(updateGoodsDTO.getStorageTemperature());
            goods.setLocation(updateGoodsDTO.getLocation());
            goods.setStatus(updateGoodsDTO.getStatus());
            
            if (updateGoodsDTO.getQuantity() != null) {
                goods.setQuantity(updateGoodsDTO.getQuantity());
            }
            
            if (updateGoodsDTO.getThreshold() != null) {
                goods.setThreshold(updateGoodsDTO.getThreshold());
            }
            
            goods.setExpiryDate(updateGoodsDTO.getExpiryDate());
            goods.setSupplierName(updateGoodsDTO.getSupplierName());
            goods.setSupplierContact(updateGoodsDTO.getSupplierContact());
            goods.setUpdatedAt(LocalDateTime.now());

            goodsRepository.save(goods);

            return ResponseEntity.ok(goods);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("商品更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteGoods(Long id) {
        try {
            if (!goodsRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("商品不存在");
            }

            goodsRepository.deleteById(id);
            return ResponseEntity.ok("商品删除成功");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("商品删除失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getGoodsById(Long id) {
        try {
            Optional<Goods> goods = goodsRepository.findById(id);
            if (goods.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("商品不存在");
            }
            return ResponseEntity.ok(goods.get());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("查询失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAllGoods() {
        try {
            List<Goods> goodsList = goodsRepository.findAll();
            return ResponseEntity.ok(goodsList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("查询失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> searchGoods(GoodsQueryDTO queryDTO) {
        try {
            List<Goods> goodsList = goodsRepository.findAll();
            
            // 简单过滤（实际项目中应该使用JPA Specification或QueryDSL）
            List<Goods> filteredList = goodsList.stream()
                    .filter(goods -> {
                        boolean match = true;
                        
                        if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().isEmpty()) {
                            match = match && (goods.getName().contains(queryDTO.getKeyword()) ||
                                           goods.getSku().contains(queryDTO.getKeyword()));
                        }
                        
                        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
                            match = match && goods.getStatus().equals(queryDTO.getStatus());
                        }
                        
                        if (queryDTO.getStorageTemperature() != null && !queryDTO.getStorageTemperature().isEmpty()) {
                            match = match && goods.getStorageTemperature().equals(queryDTO.getStorageTemperature());
                        }
                        
                        if (queryDTO.getLocation() != null && !queryDTO.getLocation().isEmpty()) {
                            match = match && goods.getLocation().contains(queryDTO.getLocation());
                        }
                        
                        if (queryDTO.getNearExpiry() != null && queryDTO.getNearExpiry()) {
                            LocalDate warningDate = LocalDate.now().plusDays(30);
                            match = match && goods.getExpiryDate() != null && 
                                    goods.getExpiryDate().isBefore(warningDate);
                        }
                        
                        if (queryDTO.getLowStock() != null && queryDTO.getLowStock()) {
                            match = match && goods.getThreshold() != null && 
                                    goods.getQuantity() <= goods.getThreshold();
                        }
                        
                        return match;
                    })
                    .toList();
            
            return ResponseEntity.ok(filteredList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateStock(Long id, Integer quantityChange, String operationType) {
        try {
            Optional<Goods> optionalGoods = goodsRepository.findById(id);
            if (optionalGoods.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("商品不存在");
            }

            Goods goods = optionalGoods.get();
            int newQuantity = goods.getQuantity() + quantityChange;
            
            if (newQuantity < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("库存数量不能小于0");
            }

            goods.setQuantity(newQuantity);
            goods.setUpdatedAt(LocalDateTime.now());
            goodsRepository.save(goods);

            return ResponseEntity.ok(goods);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("库存更新失败：" + e.getMessage());
        }
    }

    @Override
    public List<Goods> getGoodsByExpiryDate() {
        return goodsRepository.findAllByOrderByExpiryDateAsc();
    }

    @Override
    public ResponseEntity<?> getLowStockGoods() {
        try {
            List<Goods> allGoods = goodsRepository.findAll();
            List<Goods> lowStockGoods = allGoods.stream()
                    .filter(goods -> goods.getThreshold() != null && 
                            goods.getQuantity() <= goods.getThreshold())
                    .toList();
            
            return ResponseEntity.ok(lowStockGoods);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("查询失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getExpiringGoods(Integer days) {
        try {
            if (days == null) {
                days = 30; // 默认30天内过期
            }
            
            LocalDate warningDate = LocalDate.now().plusDays(days);
            List<Goods> allGoods = goodsRepository.findAll();
            List<Goods> expiringGoods = allGoods.stream()
                    .filter(goods -> goods.getExpiryDate() != null && 
                            !goods.getExpiryDate().isBefore(LocalDate.now()) &&
                            goods.getExpiryDate().isBefore(warningDate))
                    .toList();
            
            return ResponseEntity.ok(expiringGoods);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("查询失败：" + e.getMessage());
        }
    }
}
