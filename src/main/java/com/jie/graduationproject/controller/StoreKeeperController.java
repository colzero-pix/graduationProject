package com.jie.graduationproject.controller;

import com.jie.graduationproject.model.dto.AddGoodsDTO;
import com.jie.graduationproject.model.dto.AddGoodsWithLocationDTO;
import com.jie.graduationproject.model.dto.AddShelfDTO;
import com.jie.graduationproject.model.dto.GoodsQueryDTO;
import com.jie.graduationproject.model.dto.InventoryLocationDTO;
import com.jie.graduationproject.model.dto.ShelfQueryDTO;
import com.jie.graduationproject.model.dto.UpdateGoodsDTO;
import com.jie.graduationproject.model.dto.UpdateShelfDTO;
import com.jie.graduationproject.service.Goods.GoodsService;
import com.jie.graduationproject.service.Goods.Impl.GoodsServiceImpl;
import com.jie.graduationproject.service.InventoryLocation.InventoryLocationService;
import com.jie.graduationproject.service.Shelf.ShelfService;
import com.jie.graduationproject.service.Shelf.Impl.ShelfServiceImpl;
import com.jie.graduationproject.service.ShelfLevel.ShelfLevelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store-keeper")
public class StoreKeeperController {

    private final ShelfServiceImpl shelfServiceImpl;
    private final GoodsServiceImpl goodsServiceImpl;
    private final ShelfLevelService shelfLevelService;
    private final InventoryLocationService inventoryLocationService;

    @Autowired
    public StoreKeeperController(
            ShelfServiceImpl shelfServiceImpl, 
            GoodsServiceImpl goodsServiceImpl,
            ShelfLevelService shelfLevelService,
            InventoryLocationService inventoryLocationService) {
        this.shelfServiceImpl = shelfServiceImpl;
        this.goodsServiceImpl = goodsServiceImpl;
        this.shelfLevelService = shelfLevelService;
        this.inventoryLocationService = inventoryLocationService;
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

    // ========== 商品管理接口 ==========
    
    // 添加商品（仅管理员）
    @PostMapping("/goods")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addGoods(@RequestBody @Valid AddGoodsDTO addGoodsDTO) {
        return goodsServiceImpl.addGoods(addGoodsDTO);
    }

    // 更新商品信息
    @PutMapping("/goods/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_KEEPER')")
    public ResponseEntity<?> updateGoods(@PathVariable Long id, @RequestBody @Valid UpdateGoodsDTO updateGoodsDTO) {
        return goodsServiceImpl.updateGoods(id, updateGoodsDTO);
    }

    // 删除商品
    @DeleteMapping("/goods/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteGoods(@PathVariable Long id) {
        return goodsServiceImpl.deleteGoods(id);
    }

    // 根据ID查询商品
    @GetMapping("/goods/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getGoodsById(@PathVariable Long id) {
        return goodsServiceImpl.getGoodsById(id);
    }

    // 查询所有商品
    @GetMapping("/goods")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllGoods() {
        return goodsServiceImpl.getAllGoods();
    }

    // 搜索商品
    @PostMapping("/goods/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> searchGoods(@RequestBody GoodsQueryDTO queryDTO) {
        return goodsServiceImpl.searchGoods(queryDTO);
    }

    // 更新库存
    @PutMapping("/goods/{id}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_KEEPER')")
    public ResponseEntity<?> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantityChange,
            @RequestParam String operationType) {
        return goodsServiceImpl.updateStock(id, quantityChange, operationType);
    }

    // 获取低库存商品
    @GetMapping("/goods/low-stock")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getLowStockGoods() {
        return goodsServiceImpl.getLowStockGoods();
    }

    // 获取即将过期商品
    @GetMapping("/goods/expiring")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getExpiringGoods(@RequestParam(defaultValue = "7") Integer days) {
        return goodsServiceImpl.getExpiringGoods(days);
    }
    
    // 查询商品详情（包含库存位置信息）
    @GetMapping("/goods/{id}/detail")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getGoodsDetail(@PathVariable Long id) {
        return goodsServiceImpl.getGoodsDetail(id);
    }

    // ========== 货架层管理接口 ==========
    
    // 获取货架的所有层
    @GetMapping("/shelves/{shelfId}/levels")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getShelfLevels(@PathVariable Long shelfId) {
        return shelfLevelService.getShelfLevels(shelfId);
    }
    
    // 为货架创建层级
    @PostMapping("/shelf-levels/create-for-shelf/{shelfId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createLevelsForShelf(
            @PathVariable Long shelfId,
            @RequestParam Integer totalLevels) {
        return shelfLevelService.createLevelsForShelf(shelfId, totalLevels);
    }
    
    // 获取货架层详情
    @GetMapping("/shelf-levels/{levelId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getShelfLevelById(@PathVariable Long levelId) {
        return shelfLevelService.getShelfLevelById(levelId);
    }

    // 获取所有货架层列表（用于移库下拉框等）
    @GetMapping("/shelf-levels/list-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllShelfLevels() {
        return shelfLevelService.getAllShelfLevels();
    }

    // 获取可用货架层
    @GetMapping("/shelf-levels/available")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAvailableShelfLevels(
            @RequestParam(required = false) Long shelfId,
            @RequestParam(required = false) Integer minCapacity) {
        return shelfLevelService.getAvailableShelfLevels(shelfId, minCapacity);
    }
    
    // 获取货架层使用统计
    @GetMapping("/shelf-levels/{levelId}/statistics")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getShelfLevelStatistics(@PathVariable Long levelId) {
        return shelfLevelService.getShelfLevelStatistics(levelId);
    }
    
    // 获取适合商品的货架层
    @GetMapping("/shelf-levels/suitable")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getSuitableLevelsForGoods(
            @RequestParam Long goodsId,
            @RequestParam(required = false) String storageTemperature) {
        return shelfLevelService.getSuitableLevelsForGoods(goodsId, storageTemperature);
    }

    // ========== 库存位置管理接口 ==========
    
    // 添加商品到多个位置（四层库存管理）
    @PostMapping("/inventory/add-with-locations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addGoodsWithLocations(@RequestBody @Valid AddGoodsWithLocationDTO request) {
        return inventoryLocationService.addGoodsWithLocation(request);
    }
    
    // 获取商品的库存位置
    @GetMapping("/inventory/goods/{goodsId}/locations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getGoodsLocations(@PathVariable Long goodsId) {
        return inventoryLocationService.getGoodsLocations(goodsId);
    }
    
    // 获取货架层的库存
    @GetMapping("/inventory/shelf-levels/{levelId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getLevelInventory(@PathVariable Long levelId) {
        return inventoryLocationService.getLevelInventory(levelId);
    }
    
    // 移动库存
    @PostMapping("/inventory/move")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_KEEPER')")
    public ResponseEntity<?> moveInventory(
            @RequestParam Long fromLocationId,
            @RequestParam Long toShelfLevelId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String position) {
        return inventoryLocationService.moveInventory(fromLocationId, toShelfLevelId, quantity, position);
    }
    
    // 出库
    @PostMapping("/inventory/outbound")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_KEEPER')")
    public ResponseEntity<?> outboundInventory(
            @RequestParam Long locationId,
            @RequestParam Integer quantity) {
        return inventoryLocationService.outboundInventory(locationId, quantity);
    }
    
    // 入库（添加到现有库存位置）
    @PostMapping("/inventory/inbound")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_KEEPER')")
    public ResponseEntity<?> inboundInventory(
            @RequestParam Long locationId,
            @RequestParam Integer quantity) {
        return inventoryLocationService.inboundInventory(locationId, quantity);
    }
    
    // 新商品入库（创建新的库存位置）
    @PostMapping("/inventory/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addInventory(@RequestBody Map<String, Object> request) {
        try {
            Long goodsId = Long.parseLong(request.get("goodsId").toString());
            Long shelfLevelId = Long.parseLong(request.get("shelfLevelId").toString());
            Integer quantity = Integer.parseInt(request.get("quantity").toString());
            String position = (String) request.get("position");
            String batchNumber = (String) request.get("batchNumber");
            String storageDateStr = (String) request.get("storageDate");
            String expiryDateStr = (String) request.get("expiryDate");
            
            // 直接调用服务方法创建库存位置
            return inventoryLocationService.createInventoryLocation(goodsId, shelfLevelId, quantity, 
                    position, batchNumber, storageDateStr, expiryDateStr);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("请求参数错误: " + e.getMessage());
        }
    }
    
    // 更新库存位置信息
    @PutMapping("/inventory/locations/{locationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_KEEPER')")
    public ResponseEntity<?> updateInventoryLocation(
            @PathVariable Long locationId,
            @RequestBody @Valid InventoryLocationDTO locationDTO) {
        return inventoryLocationService.updateInventoryLocation(locationId, locationDTO);
    }
    
    // 删除库存位置
    @DeleteMapping("/inventory/locations/{locationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteInventoryLocation(@PathVariable Long locationId) {
        return inventoryLocationService.deleteInventoryLocation(locationId);
    }
    
    // 获取即将过期库存
    @GetMapping("/inventory/expiring")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getExpiringInventory(@RequestParam(defaultValue = "7") Integer days) {
        return inventoryLocationService.getExpiringInventory(days);
    }
    
    // 获取先进先出出库建议
    @GetMapping("/inventory/fifo-suggestions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getFIFOOutboundSuggestions(
            @RequestParam Long goodsId,
            @RequestParam Integer requiredQuantity) {
        return inventoryLocationService.getFIFOOutboundSuggestions(goodsId, requiredQuantity);
    }
    
    // 获取库存统计
    @GetMapping("/inventory/statistics")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getInventoryStatistics() {
        return inventoryLocationService.getInventoryStatistics();
    }
    
    // 搜索库存
    @GetMapping("/inventory/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> searchInventory(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String shelfType,
            @RequestParam(required = false) LocalDate expiryDate) {
        return inventoryLocationService.searchInventory(keyword, area, shelfType, expiryDate);
    }
    
    // 批量更新库存状态
    @PostMapping("/inventory/batch-update-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_KEEPER')")
    public ResponseEntity<?> batchUpdateStatus(
            @RequestParam List<Long> locationIds,
            @RequestParam String status) {
        return inventoryLocationService.batchUpdateStatus(locationIds, status);
    }
}
