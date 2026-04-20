package com.jie.graduationproject.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InventoryLocationDTO {

    private Long id;
    private Long goodsId;
    private String goodsName;
    private String sku;
    private Long shelfLevelId;
    private String shelfCode;
    private String area;
    private Integer levelNumber;
    private String levelName;
    private String fullLocation;
    private Integer quantity;
    private String position;
    private String batchNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate storageDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private String status;
    private String remarks;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Getter和Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getShelfLevelId() {
        return shelfLevelId;
    }

    public void setShelfLevelId(Long shelfLevelId) {
        this.shelfLevelId = shelfLevelId;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(Integer levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getFullLocation() {
        return fullLocation;
    }

    public void setFullLocation(String fullLocation) {
        this.fullLocation = fullLocation;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(LocalDate storageDate) {
        this.storageDate = storageDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // 静态工厂方法从实体创建DTO
    public static InventoryLocationDTO fromEntity(com.jie.graduationproject.model.entity.InventoryLocation entity) {
        InventoryLocationDTO dto = new InventoryLocationDTO();
        dto.setId(entity.getId());
        
        if (entity.getGoods() != null) {
            dto.setGoodsId(entity.getGoods().getId());
            dto.setGoodsName(entity.getGoods().getName());
            dto.setSku(entity.getGoods().getSku());
        }
        
        if (entity.getShelfLevel() != null) {
            dto.setShelfLevelId(entity.getShelfLevel().getId());
            dto.setLevelNumber(entity.getShelfLevel().getLevelNumber());
            dto.setLevelName(entity.getShelfLevel().getLevelName());
            
            if (entity.getShelfLevel().getShelf() != null) {
                dto.setShelfCode(entity.getShelfLevel().getShelf().getShelfCode());
                dto.setArea(entity.getShelfLevel().getShelf().getArea());
            }
        }
        
        dto.setFullLocation(entity.getFullLocation());
        dto.setQuantity(entity.getQuantity());
        dto.setPosition(entity.getPosition());
        dto.setBatchNumber(entity.getBatchNumber());
        dto.setStorageDate(entity.getStorageDate());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }
}