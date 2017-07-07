package com.autoask.entity.mysql;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
public class OrderServe {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private String mechanicId;

    private String serviceProviderId;

    private BigDecimal serviceProviderPreShare;

    private Short rate;

    @Transient
    private OrderAppointValidate orderAppointValidate;

    @Transient
    private String serviceProviderName;

    @Transient
    private String mechanicName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(String mechanicId) {
        this.mechanicId = mechanicId;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public BigDecimal getServiceProviderPreShare() {
        return serviceProviderPreShare;
    }

    public void setServiceProviderPreShare(BigDecimal serviceProviderPreShare) {
        this.serviceProviderPreShare = serviceProviderPreShare;
    }

    public Short getRate() {
        return rate;
    }

    public void setRate(Short rate) {
        this.rate = rate;
    }

    public OrderAppointValidate getOrderAppointValidate() {
        return orderAppointValidate;
    }

    public void setOrderAppointValidate(OrderAppointValidate orderAppointValidate) {
        this.orderAppointValidate = orderAppointValidate;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getMechanicName() {
        return mechanicName;
    }

    public void setMechanicName(String mechanicName) {
        this.mechanicName = mechanicName;
    }
}