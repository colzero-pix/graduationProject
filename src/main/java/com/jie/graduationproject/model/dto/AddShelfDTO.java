package com.jie.graduationproject.model.dto;

import lombok.Data;

@Data
public class AddShelfDTO {

    //货架编号（如：A-01、B-02）
    private String shelfCode;

    //货架名称（如：饮料区货架A）
    private String shelfName;

    //所属区域（常温区/冰冻区）
    private String area;

    //货架类型（高频快消区/中频干货区/低频重物区）
    private String shelfType;

    //具体位置描述（如：靠交通要道、靠窗、角落等）
    private String locationDesc;

    //所在楼层/层数
    private Integer floor;

    //货架总层数
    private Integer totalLevels;

    //每层高度（厘米）
    private Double levelHeight;

    //货架长度（厘米）
    private Double length;

    //货架深度（厘米）
    private Double depth;

    //最大承重（千克）
    private Double maxWeight;

    //当前存放的商品种类数量
    private Integer productTypesCount;

    //当前存放的商品总数量
    private Integer totalQuantity;

    //最下层是否用于存放重物
    private Boolean isBottomForHeavy;

    //货架状态（正常/维修中/已满/空置）
    private String status;

    //主要存放的商品类别（如：饮料、零食、日用品）
    private String mainCategory;
}
