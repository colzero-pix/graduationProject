package com.jie.graduationproject.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_location")
public class InventoryLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelf_level_id", nullable = false)
    private ShelfLevel shelfLevel;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "position")
    private String position; // 具体位置：左侧/右侧/中间/前部/后部

    @Column(name = "batch_number")
    private String batchNumber; // 批次号

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "storage_date")
    private LocalDate storageDate; // 该批次入库日期

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expiry_date")
    private LocalDate expiryDate; // 该批次过期日期

    @Column(name = "status")
    private String status; // 状态：正常/待出库/已出库/冻结

    @Column(name = "remarks")
    private String remarks;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public ShelfLevel getShelfLevel() {
        return shelfLevel;
    }

    public void setShelfLevel(ShelfLevel shelfLevel) {
        this.shelfLevel = shelfLevel;
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

    // 获取完整的货架位置信息
    public String getFullLocation() {
        if (shelfLevel == null || shelfLevel.getShelf() == null) {
            return "未知位置";
        }
        
        String shelfCode = shelfLevel.getShelf().getShelfCode();
        String levelName = shelfLevel.getLevelName();
        
        StringBuilder location = new StringBuilder();
        location.append(shelfCode);
        
        if (levelName != null && !levelName.isEmpty()) {
            location.append(" ").append(levelName);
        } else if (shelfLevel.getLevelNumber() != null) {
            location.append(" 第").append(shelfLevel.getLevelNumber()).append("层");
        }
        
        if (position != null && !position.isEmpty()) {
            location.append(" ").append(position);
        }
        
        return location.toString();
    }
}