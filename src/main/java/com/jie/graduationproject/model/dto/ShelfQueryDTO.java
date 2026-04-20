package com.jie.graduationproject.model.dto;

import lombok.Data;

@Data
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
}