package com.autoask.entity.mysql;

import com.autoask.entity.mongo.comment.OrderComment;
import com.autoask.entity.mongo.invoice.Invoice;
import com.autoask.entity.mongo.log.OrderLog;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class OrderInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    /**
     * 用户下单的时候，会拆分成多个订单
     * serial用于表示多个订单同时进行的id
     */
    private String serial;

    private String userId;

    private BigDecimal snapshotPrice;

    private BigDecimal deliveryFee;

    private BigDecimal discountPrice;

    private BigDecimal payPrice;

    /**
     * online
     * offline
     */
    private String serveType;

    /**
     *
     */
    private String status;

    private String payType;

    private String paySerial;

    private Date payTime;

    private String refundSerial;

    private String refundType;

    private String refundId;

    private Date refundTime;

    private Date createTime;

    private Date receiveTime;

    private Date commentTime;

    private Boolean userDeleteFlag;

    private String invoiceId;

    private String shareStatus;

    private Date shareTime;

    private String shareOperatorId;

    private Integer commentRate;

    //服务费
    private BigDecimal serviceFee;

    /**
     * 用户信息
     */
    @Transient
    private User user;

    @Transient
    private OrderDelivery orderDelivery;

    @Transient
    private OrderServe orderServe;

    @Transient
    private OrderAppointValidate orderAppointValidate;

    @Transient
    private List<OrderGoods> orderGoodsList;

    @Transient
    private OrderShare orderShare;

    /**
     * 为了后台展示方便添加的冗余字段
     * 一个订单中使用的所有的卡的id
     */
    @Transient
    private List<String> cardIdList;

    @Transient
    private List<OrderLog> orderLogList;

    @Transient
    private OrderComment orderComment;

    @Transient
    private Invoice invoice;

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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getSnapshotPrice() {
        return snapshotPrice;
    }

    public void setSnapshotPrice(BigDecimal snapshotPrice) {
        this.snapshotPrice = snapshotPrice;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public String getServeType() {
        return serveType;
    }

    public void setServeType(String serveType) {
        this.serveType = serveType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPaySerial() {
        return paySerial;
    }

    public void setPaySerial(String paySerial) {
        this.paySerial = paySerial;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getRefundSerial() {
        return refundSerial;
    }

    public void setRefundSerial(String refundSerial) {
        this.refundSerial = refundSerial;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Boolean getUserDeleteFlag() {
        return userDeleteFlag;
    }

    public void setUserDeleteFlag(Boolean userDeleteFlag) {
        this.userDeleteFlag = userDeleteFlag;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(String shareStatus) {
        this.shareStatus = shareStatus;
    }

    public Date getShareTime() {
        return shareTime;
    }

    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
    }

    public String getShareOperatorId() {
        return shareOperatorId;
    }

    public void setShareOperatorId(String shareOperatorId) {
        this.shareOperatorId = shareOperatorId;
    }

    public Integer getCommentRate() {
        return commentRate;
    }

    public void setCommentRate(Integer commentRate) {
        this.commentRate = commentRate;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderDelivery getOrderDelivery() {
        return orderDelivery;
    }

    public void setOrderDelivery(OrderDelivery orderDelivery) {
        this.orderDelivery = orderDelivery;
    }

    public OrderServe getOrderServe() {
        return orderServe;
    }

    public void setOrderServe(OrderServe orderServe) {
        this.orderServe = orderServe;
    }

    public OrderAppointValidate getOrderAppointValidate() {
        return orderAppointValidate;
    }

    public void setOrderAppointValidate(OrderAppointValidate orderAppointValidate) {
        this.orderAppointValidate = orderAppointValidate;
    }

    public List<OrderGoods> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<OrderGoods> orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
    }

    public OrderShare getOrderShare() {
        return orderShare;
    }

    public void setOrderShare(OrderShare orderShare) {
        this.orderShare = orderShare;
    }

    public List<String> getCardIdList() {
        return cardIdList;
    }

    public void setCardIdList(List<String> cardIdList) {
        this.cardIdList = cardIdList;
    }

    public List<OrderLog> getOrderLogList() {
        return orderLogList;
    }

    public void setOrderLogList(List<OrderLog> orderLogList) {
        this.orderLogList = orderLogList;
    }

    public OrderComment getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(OrderComment orderComment) {
        this.orderComment = orderComment;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}