package com.jie.graduationproject.service.Shelf;

import com.jie.graduationproject.model.dto.AddShelfDTO;
import com.jie.graduationproject.model.dto.ShelfQueryDTO;
import com.jie.graduationproject.model.dto.UpdateShelfDTO;
import org.springframework.http.ResponseEntity;

public interface ShelfService {
    
    // 添加货架
    ResponseEntity<?> addShelf(AddShelfDTO addShelfDTO);
    
    // 更新货架信息
    ResponseEntity<?> updateShelf(Long id, UpdateShelfDTO updateShelfDTO);
    
    // 删除货架
    ResponseEntity<?> deleteShelf(Long id);
    
    // 根据ID查询货架
    ResponseEntity<?> getShelfById(Long id);
    
    // 查询所有货架
    ResponseEntity<?> getAllShelves();
    
    // 搜索货架
    ResponseEntity<?> searchShelves(ShelfQueryDTO queryDTO);
    
    // 分配商品到货架
    ResponseEntity<?> assignGoodsToShelf(Long shelfId, Long goodsId, String location);
    
    // 获取货架使用率
    ResponseEntity<?> getShelfUtilization(Long id);
    
    // 获取可用货架
    ResponseEntity<?> getAvailableShelves(String area, String shelfType);
}