package com.jie.graduationproject.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "operation_log")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_type", nullable = false, length = 20)
    private String operationType;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "goods_name", length = 100)
    private String goodsName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "operator", length = 50)
    private String operator;

    @Column(name = "operator_role", length = 20)
    private String operatorRole;

    @Column(name = "location_info", length = 200)
    private String locationInfo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public OperationLog() {
    }

    public OperationLog(String operationType, String description, String goodsName, Integer quantity,
                        String operator, String operatorRole, String locationInfo) {
        this.operationType = operationType;
        this.description = description;
        this.goodsName = goodsName;
        this.quantity = quantity;
        this.operator = operator;
        this.operatorRole = operatorRole;
        this.locationInfo = locationInfo;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String goodsName) { this.goodsName = goodsName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getOperatorRole() { return operatorRole; }
    public void setOperatorRole(String operatorRole) { this.operatorRole = operatorRole; }
    public String getLocationInfo() { return locationInfo; }
    public void setLocationInfo(String locationInfo) { this.locationInfo = locationInfo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
