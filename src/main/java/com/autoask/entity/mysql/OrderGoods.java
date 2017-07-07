package com.autoask.entity.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class OrderGoods {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderGoodsId;

    private String orderId;

    private String goodsSnapshotId;

    private Integer num;

    private BigDecimal snapshotPrice;

    private BigDecimal discountPrice;

    private BigDecimal payPrice;


    @Transient
    private GoodsSnapshot goodsSnapshot;

    @Transient
    private List<OrderGoodsCard> orderGoodsCardList;

    @Transient
    private OrderGoodsShare orderGoodsShare;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(String orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodsSnapshotId() {
        return goodsSnapshotId;
    }

    public void setGoodsSnapshotId(String goodsSnapshotId) {
        this.goodsSnapshotId = goodsSnapshotId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getSnapshotPrice() {
        return snapshotPrice;
    }

    public void setSnapshotPrice(BigDecimal snapshotPrice) {
        this.snapshotPrice = snapshotPrice;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public GoodsSnapshot getGoodsSnapshot() {
        return goodsSnapshot;
    }

    public void setGoodsSnapshot(GoodsSnapshot goodsSnapshot) {
        this.goodsSnapshot = goodsSnapshot;
    }

    public List<OrderGoodsCard> getOrderGoodsCardList() {
        return orderGoodsCardList;
    }

    public void setOrderGoodsCardList(List<OrderGoodsCard> orderGoodsCardList) {
        this.orderGoodsCardList = orderGoodsCardList;
    }

    public OrderGoodsShare getOrderGoodsShare() {
        return orderGoodsShare;
    }

    public void setOrderGoodsShare(OrderGoodsShare orderGoodsShare) {
        this.orderGoodsShare = orderGoodsShare;
    }
}