package com.jie.graduationproject.controller;

import com.jie.graduationproject.model.dto.AddShelfDTO;
import com.jie.graduationproject.model.dto.ShelfQueryDTO;
import com.jie.graduationproject.model.dto.UpdateShelfDTO;
import com.jie.graduationproject.service.Shelf.ShelfService;
import com.jie.graduationproject.service.Shelf.Impl.ShelfServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store-keeper")
public class StoreKeeperController {

    private final ShelfServiceImpl shelfServiceImpl;

    @Autowired
    public StoreKeeperController(ShelfServiceImpl shelfServiceImpl) {
        this.shelfServiceImpl = shelfServiceImpl;
    }

    // 添加货架（仅管理员）
    @PostMapping("/shelves")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addShelf(@RequestBody @Valid AddShelfDTO addShelfDTO) {
        return shelfServiceImpl.addShelf(addShelfDTO);
    }

    // 更新货架信息
    @PutMapping("/shelves/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_KEEPER')")
    public ResponseEntity<?> updateShelf(@PathVariable Long id, @RequestBody @Valid UpdateShelfDTO updateShelfDTO) {
        return shelfServiceImpl.updateShelf(id, updateShelfDTO);
    }

    // 删除货架
    @DeleteMapping("/shelves/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteShelf(@PathVariable Long id) {
        return shelfServiceImpl.deleteShelf(id);
    }

    // 根据ID查询货架
    @GetMapping("/shelves/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getShelfById(@PathVariable Long id) {
        return shelfServiceImpl.getShelfById(id);
    }

    // 查询所有货架
    @GetMapping("/shelves")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllShelves() {
        return shelfServiceImpl.getAllShelves();
    }

    // 搜索货架
    @PostMapping("/shelves/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> searchShelves(@RequestBody ShelfQueryDTO queryDTO) {
        return shelfServiceImpl.searchShelves(queryDTO);
    }

    // 分配商品到货架
    @PostMapping("/shelves/{shelfId}/assign-goods/{goodsId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_KEEPER')")
    public ResponseEntity<?> assignGoodsToShelf(
            @PathVariable Long shelfId,
            @PathVariable Long goodsId,
            @RequestParam String location) {
        return shelfServiceImpl.assignGoodsToShelf(shelfId, goodsId, location);
    }

    // 获取货架使用率
    @GetMapping("/shelves/{id}/utilization")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getShelfUtilization(@PathVariable Long id) {
        return shelfServiceImpl.getShelfUtilization(id);
    }

    // 获取可用货架
    @GetMapping("/shelves/available")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAvailableShelves(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String shelfType) {
        return shelfServiceImpl.getAvailableShelves(area, shelfType);
    }
}
