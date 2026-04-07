package com.jie.graduationproject.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "goods")
@Data
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    //当前剩余数量
    private Integer quantity;

    //库存预警阈值（低于此值提醒进货）
    private Integer threshold;

    //入库日期，用于先进先出管理
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate storageDate;

    //有效期/过期日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    //供应商名称
    private String supplierName;

    //供应商联系方式（电话等）
    private String supplierContact;

    //记录创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    //最后更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}
