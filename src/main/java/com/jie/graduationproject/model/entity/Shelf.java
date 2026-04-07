package com.jie.graduationproject.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "shelf")
@Data
public class Shelf {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        //货架编号（如：A-01、B-02）
        @Column(nullable = false, unique = true, length = 20)
        private String shelfCode;

        //货架名称（如：饮料区货架A）
        @Column(length = 50)
        private String shelfName;

        //所属区域（常温区/冰冻区）
        @Column(nullable = false, length = 20)
        private String area;

        //货架类型（高频快消区/中频干货区/低频重物区）
        @Column(nullable = false, length = 20)
        private String shelfType;

        //具体位置描述（如：靠交通要道、靠窗、角落等）
        @Column(length = 100)
        private String locationDesc;

        //所在楼层/层数
        private Integer floor;

//        //通道号（如：主通道A、副通道B）
//        @Column(length = 20)
//        private String aisleNumber;

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
        @Column(nullable = false, length = 20)
        private String status;

        //主要存放的商品类别（如：饮料、零食、日用品）
        @Column(length = 50)
        private String mainCategory;

        //备注信息
        @Column(length = 500)
        private String remarks;

        //创建时间
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        //更新时间
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Column(nullable = false)
        private LocalDateTime updatedAt;

}
