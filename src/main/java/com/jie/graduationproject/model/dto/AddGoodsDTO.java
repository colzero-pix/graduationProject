package com.jie.graduationproject.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class AddGoodsDTO {

    //商品唯一编码（如：6901234567890）
    private String sku;

    //货物名称
    private String name;

    //存放温度区域（常温/冰冻）
    private String storageTemperature;

    //货架位置（如：A-3 表示A区第3层）
    private String location;

    //货物状态（正常/待退货/残损）
    private String status;

    //库存预警阈值（低于此值提醒进货）
    private Integer threshold;

    //保质期（月数，如6表示6个月，12表示1年）
    private Integer shelfLifeMonths;

    //供应商名称
    private String supplierName;

    //供应商联系方式（电话等）
    private String supplierContact;

    // Getter和Setter方法
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

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

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getShelfLifeMonths() {
        return shelfLifeMonths;
    }

    public void setShelfLifeMonths(Integer shelfLifeMonths) {
        this.shelfLifeMonths = shelfLifeMonths;
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
