package com.jie.graduationproject.model.dto;

public class GoodsQueryDTO {

    private String keyword;

    private String status;

    private String storageTemperature;

    private String location;

    private Boolean nearExpiry;

    private Boolean lowStock;

    // Getter和Setter方法
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStorageTemperature() {
        return storageTemperature;
    }

    public void setStorageTemperature(String storageTemperature) {
        this.storageTemperature = storageTemperature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getNearExpiry() {
        return nearExpiry;
    }

    public void setNearExpiry(Boolean nearExpiry) {
        this.nearExpiry = nearExpiry;
    }

    public Boolean getLowStock() {
        return lowStock;
    }

    public void setLowStock(Boolean lowStock) {
        this.lowStock = lowStock;
    }
}
