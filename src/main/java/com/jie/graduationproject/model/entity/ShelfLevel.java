package com.jie.graduationproject.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shelf_level")
public class ShelfLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelf_id", nullable = false)
    private Shelf shelf;

    @Column(name = "level_number", nullable = false)
    private Integer levelNumber;

    @Column(name = "level_name")
    private String levelName;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "current_quantity", columnDefinition = "INT DEFAULT 0")
    private Integer currentQuantity = 0;

    @Column(name = "status")
    private String status; // 状态：空闲/部分占用/已满/禁用

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

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
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

    // 计算剩余容量
    public Integer getRemainingCapacity() {
        if (capacity == null || currentQuantity == null) {
            return 0;
        }
        return Math.max(0, capacity - currentQuantity);
    }

    // 更新状态
    public void updateStatus() {
        if (capacity == null || capacity == 0) {
            this.status = "禁用";
            return;
        }
        
        if (currentQuantity == null || currentQuantity == 0) {
            this.status = "空闲";
        } else if (currentQuantity >= capacity) {
            this.status = "已满";
        } else {
            this.status = "部分占用";
        }
    }
}