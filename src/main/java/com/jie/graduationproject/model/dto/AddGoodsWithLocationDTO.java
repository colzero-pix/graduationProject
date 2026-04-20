package com.jie.graduationproject.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public class AddGoodsWithLocationDTO {

    @Valid
    @NotNull(message = "商品信息不能为空")
    private GoodsInfo goods;

    @Valid
    @NotNull(message = "存放位置不能为空")
    private List<LocationInfo> locations;

    // 商品信息内部类
    public static class GoodsInfo {
        @NotBlank(message = "商品编码不能为空")
        private String sku;

        @NotBlank(message = "商品名称不能为空")
        private String name;

        @NotBlank(message = "存放温度不能为空")
        private String storageTemperature;

        @NotNull(message = "商品状态不能为空")
        private String status;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "入库日期不能为空")
        private LocalDate storageDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "过期日期不能为空")
        private LocalDate expiryDate;

        private String supplierName;
        private String supplierContact;

        // Getter和Setter
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

    // 存放位置信息内部类
    public static class LocationInfo {
        @NotNull(message = "货架层ID不能为空")
        private Long shelfLevelId;

        @NotNull(message = "数量不能为空")
        @Positive(message = "数量必须大于0")
        private Integer quantity;

        private String position; // 具体位置：左侧/右侧/中间
        private String batchNumber; // 批次号

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate storageDate; // 该批次入库日期

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate expiryDate; // 该批次过期日期

        // Getter和Setter
        public Long getShelfLevelId() {
            return shelfLevelId;
        }

        public void setShelfLevelId(Long shelfLevelId) {
            this.shelfLevelId = shelfLevelId;
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
    }

    // Getter和Setter
    public GoodsInfo getGoods() {
        return goods;
    }

    public void setGoods(GoodsInfo goods) {
        this.goods = goods;
    }

    public List<LocationInfo> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationInfo> locations) {
        this.locations = locations;
    }

    // 验证总数量是否匹配
    public boolean validateTotalQuantity() {
        if (locations == null || locations.isEmpty()) {
            return false;
        }
        
        int total = locations.stream()
                .mapToInt(LocationInfo::getQuantity)
                .sum();
        
        return total > 0;
    }
}