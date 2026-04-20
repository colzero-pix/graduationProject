package com.jie.graduationproject.model.dto;

import lombok.Data;

@Data
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
}