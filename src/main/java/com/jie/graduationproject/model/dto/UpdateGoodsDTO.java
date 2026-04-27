package com.jie.graduationproject.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class UpdateGoodsDTO {

    //货物名称
    private String name;

    //存放温度区域（常温/冰冻）
    private String storageTemperature;

    //货架位置（如：A-3 表示A区第3层）
    private String location;

    //货物状态（正常/待退货/残损）
    private String status;

    //货物数量
    private Integer quantity;

    //库存预警阈值（低于此值提醒进货）
    private Integer threshold;

    //有效期/过期日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    //供应商名称
    private String supplierName;

    //供应商联系方式（电话等）
    private String supplierContact;

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }
}
