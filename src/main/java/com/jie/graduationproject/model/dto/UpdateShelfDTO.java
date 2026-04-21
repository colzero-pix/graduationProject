package com.jie.graduationproject.model.dto;

public class UpdateShelfDTO {
    
    // 货架名称
    private String shelfName;
    
    // 所属区域
    private String area;
    
    // 货架类型
    private String shelfType;
    
    // 具体位置描述
    private String locationDesc;
    
    // 所在楼层/层数
    private Integer floor;
    
    // 货架总层数
    private Integer totalLevels;
    
    // 每层高度（厘米）
    private Double levelHeight;
    
    // 货架长度（厘米）
    private Double length;
    
    // 货架深度（厘米）
    private Double depth;
    
    // 最大承重（千克）
    private Double maxWeight;
    
    // 当前存放的商品种类数量
    private Integer productTypesCount;
    
    // 当前存放的商品总数量
    private Integer totalQuantity;
    
    // 最下层是否用于存放重物
    private Boolean isBottomForHeavy;
    
    // 货架状态
    private String status;
    
    // 主要存放的商品类别
    private String mainCategory;
    
    // 备注信息
    private String remarks;

    // Getter和Setter方法
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

    public String getShelfType() {
        return shelfType;
    }

    public void setShelfType(String shelfType) {
        this.shelfType = shelfType;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getTotalLevels() {
        return totalLevels;
    }

    public void setTotalLevels(Integer totalLevels) {
        this.totalLevels = totalLevels;
    }

    public Double getLevelHeight() {
        return levelHeight;
    }

    public void setLevelHeight(Double levelHeight) {
        this.levelHeight = levelHeight;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Integer getProductTypesCount() {
        return productTypesCount;
    }

    public void setProductTypesCount(Integer productTypesCount) {
        this.productTypesCount = productTypesCount;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Boolean getIsBottomForHeavy() {
        return isBottomForHeavy;
    }

    public void setIsBottomForHeavy(Boolean isBottomForHeavy) {
        this.isBottomForHeavy = isBottomForHeavy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}