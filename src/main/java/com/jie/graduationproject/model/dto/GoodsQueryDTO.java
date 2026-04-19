package com.jie.graduationproject.model.dto;

import lombok.Data;

@Data
public class GoodsQueryDTO {

    private String keyword;

    private String Status;

    private String StorageTemperature;

    private String Location;

    private Boolean NearExpiry;

    private Boolean LowStock;

}
