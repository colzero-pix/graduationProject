package com.jie.graduationproject.model.dto;

public class ShelfQueryDTO {
    
    // 搜索关键词（货架编号、名称、区域等）
    private String keyword;
    
    // 货架状态
    private String status;
    
    // 所属区域
    private String area;
    
    // 货架类型
    private String shelfType;
    
    // 主要商品类别
    private String mainCategory;
    
    // 是否查询可用货架（status = "正常" 且 totalQuantity < 阈值）
    private Boolean availableOnly;
    
    // 是否查询容量紧张的货架（使用率 > 80%）
    private Boolean highUtilization;
    
    // 是否查询空置货架
    private Boolean emptyOnly;
    
    // 楼层
    private Integer floor;
    
    // 最小承重要求
    private Double minWeightCapacity;
    
    // 排序字段
    private String sortBy;
    
    // 排序方向（asc/desc）
    private String sortDirection;
    
    // 分页参数
    private Integer page;
    private Integer size;

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

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public Boolean getAvailableOnly() {
        return availableOnly;
    }

    public void setAvailableOnly(Boolean availableOnly) {
        this.availableOnly = availableOnly;
    }

    public Boolean getHighUtilization() {
        return highUtilization;
    }

    public void setHighUtilization(Boolean highUtilization) {
        this.highUtilization = highUtilization;
    }

    public Boolean getEmptyOnly() {
        return emptyOnly;
    }

    public void setEmptyOnly(Boolean emptyOnly) {
        this.emptyOnly = emptyOnly;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Double getMinWeightCapacity() {
        return minWeightCapacity;
    }

    public void setMinWeightCapacity(Double minWeightCapacity) {
        this.minWeightCapacity = minWeightCapacity;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}