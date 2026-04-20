package com.jie.graduationproject.model.dto;

import com.jie.graduationproject.model.entity.Goods;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GoodsDetailDTO {
    
    // 商品基本信息
    private Long id;
    private String sku;
    private String name;
    private String storageTemperature;
    private String status;
    private LocalDate storageDate;
    private LocalDate expiryDate;
    private String supplierName;
    private String supplierContact;
    private Integer quantity;
    private Integer threshold;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 库存位置信息
    private List<InventoryLocationInfo> inventoryLocations = new ArrayList<>();
    
    // 统计信息
    private Integer totalLocations;
    private Integer totalQuantity;
    private String earliestExpiryDate;
    private String latestStorageDate;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getStorageTemperature() { return storageTemperature; }
    public void setStorageTemperature(String storageTemperature) { this.storageTemperature = storageTemperature; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getStorageDate() { return storageDate; }
    public void setStorageDate(LocalDate storageDate) { this.storageDate = storageDate; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    
    public String getSupplierContact() { return supplierContact; }
    public void setSupplierContact(String supplierContact) { this.supplierContact = supplierContact; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<InventoryLocationInfo> getInventoryLocations() { return inventoryLocations; }
    public void setInventoryLocations(List<InventoryLocationInfo> inventoryLocations) { this.inventoryLocations = inventoryLocations; }
    
    public Integer getTotalLocations() { return totalLocations; }
    public void setTotalLocations(Integer totalLocations) { this.totalLocations = totalLocations; }
    
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    
    public String getEarliestExpiryDate() { return earliestExpiryDate; }
    public void setEarliestExpiryDate(String earliestExpiryDate) { this.earliestExpiryDate = earliestExpiryDate; }
    
    public String getLatestStorageDate() { return latestStorageDate; }
    public void setLatestStorageDate(String latestStorageDate) { this.latestStorageDate = latestStorageDate; }
    
    public static class InventoryLocationInfo {
        private Long locationId;
        private Long shelfLevelId;
        private String shelfName;
        private String shelfCode;
        private String area;
        private String shelfType;
        private String levelName;
        private Integer levelNumber;
        private String position;
        private Integer quantity;
        private String batchNumber;
        private LocalDate storageDate;
        private LocalDate expiryDate;
        private String status;
        private String remarks;
        private LocalDateTime createdAt;
        
        // Getters and Setters
        public Long getLocationId() { return locationId; }
        public void setLocationId(Long locationId) { this.locationId = locationId; }
        
        public Long getShelfLevelId() { return shelfLevelId; }
        public void setShelfLevelId(Long shelfLevelId) { this.shelfLevelId = shelfLevelId; }
        
        public String getShelfName() { return shelfName; }
        public void setShelfName(String shelfName) { this.shelfName = shelfName; }
        
        public String getShelfCode() { return shelfCode; }
        public void setShelfCode(String shelfCode) { this.shelfCode = shelfCode; }
        
        public String getArea() { return area; }
        public void setArea(String area) { this.area = area; }
        
        public String getShelfType() { return shelfType; }
        public void setShelfType(String shelfType) { this.shelfType = shelfType; }
        
        public String getLevelName() { return levelName; }
        public void setLevelName(String levelName) { this.levelName = levelName; }
        
        public Integer getLevelNumber() { return levelNumber; }
        public void setLevelNumber(Integer levelNumber) { this.levelNumber = levelNumber; }
        
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public String getBatchNumber() { return batchNumber; }
        public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
        
        public LocalDate getStorageDate() { return storageDate; }
        public void setStorageDate(LocalDate storageDate) { this.storageDate = storageDate; }
        
        public LocalDate getExpiryDate() { return expiryDate; }
        public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
        
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
    
    public static GoodsDetailDTO fromGoods(Goods goods) {
        if (goods == null) {
            return null;
        }
        
        GoodsDetailDTO dto = new GoodsDetailDTO();
        dto.setId(goods.getId());
        dto.setSku(goods.getSku());
        dto.setName(goods.getName());
        dto.setStorageTemperature(goods.getStorageTemperature());
        dto.setStatus(goods.getStatus());
        dto.setStorageDate(goods.getStorageDate());
        dto.setExpiryDate(goods.getExpiryDate());
        dto.setSupplierName(goods.getSupplierName());
        dto.setSupplierContact(goods.getSupplierContact());
        dto.setQuantity(goods.getQuantity());
        dto.setThreshold(goods.getThreshold());
        dto.setLocation(goods.getLocation());
        dto.setCreatedAt(goods.getCreatedAt());
        dto.setUpdatedAt(goods.getUpdatedAt());
        
        return dto;
    }
    
    public void addInventoryLocation(InventoryLocationInfo locationInfo) {
        if (inventoryLocations == null) {
            inventoryLocations = new ArrayList<>();
        }
        inventoryLocations.add(locationInfo);
    }
    
    public void calculateStatistics() {
        if (inventoryLocations == null || inventoryLocations.isEmpty()) {
            totalLocations = 0;
            totalQuantity = 0;
            earliestExpiryDate = null;
            latestStorageDate = null;
            return;
        }
        
        totalLocations = inventoryLocations.size();
        
        // 计算总数量
        int sum = 0;
        for (InventoryLocationInfo location : inventoryLocations) {
            if (location.getQuantity() != null) {
                sum += location.getQuantity();
            }
        }
        totalQuantity = sum;
        
        // 计算最早过期日期
        LocalDate earliest = null;
        for (InventoryLocationInfo location : inventoryLocations) {
            if (location.getExpiryDate() != null) {
                if (earliest == null || location.getExpiryDate().isBefore(earliest)) {
                    earliest = location.getExpiryDate();
                }
            }
        }
        earliestExpiryDate = earliest != null ? earliest.toString() : null;
        
        // 计算最晚入库日期
        LocalDate latest = null;
        for (InventoryLocationInfo location : inventoryLocations) {
            if (location.getStorageDate() != null) {
                if (latest == null || location.getStorageDate().isAfter(latest)) {
                    latest = location.getStorageDate();
                }
            }
        }
        latestStorageDate = latest != null ? latest.toString() : null;
    }
}