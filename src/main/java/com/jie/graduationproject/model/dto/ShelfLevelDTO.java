package com.jie.graduationproject.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ShelfLevelDTO {

    private Long id;
    private Long shelfId;
    private String shelfCode;
    private String shelfName;
    private String area;
    private Integer levelNumber;
    private String levelName;
    private Integer capacity;
    private Integer currentQuantity;
    private Integer remainingCapacity;
    private String status;
    private String remarks;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // 商品列表（可选，用于详情页面）
    private Object goodsList;

    // Getter和Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShelfId() {
        return shelfId;
    }

    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
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

    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(Integer remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
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

    public Object getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(Object goodsList) {
        this.goodsList = goodsList;
    }

    // 静态工厂方法从实体创建DTO
    public static ShelfLevelDTO fromEntity(com.jie.graduationproject.model.entity.ShelfLevel entity) {
        ShelfLevelDTO dto = new ShelfLevelDTO();
        dto.setId(entity.getId());
        
        if (entity.getShelf() != null) {
            dto.setShelfId(entity.getShelf().getId());
            dto.setShelfCode(entity.getShelf().getShelfCode());
            dto.setShelfName(entity.getShelf().getShelfName());
            dto.setArea(entity.getShelf().getArea());
        }
        
        dto.setLevelNumber(entity.getLevelNumber());
        dto.setLevelName(entity.getLevelName());
        dto.setCapacity(entity.getCapacity());
        dto.setCurrentQuantity(entity.getCurrentQuantity());
        dto.setRemainingCapacity(entity.getRemainingCapacity());
        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }
}