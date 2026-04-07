package com.jie.graduationproject.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
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

    //货物数量
    private Integer goodsQuantity;

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

}
