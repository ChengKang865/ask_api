package com.autoask.entity.param;


import com.autoask.entity.common.Address;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author hyy
 * @create 2016-11-30 18:08
 */
public class OrderParam {

    //支付总费用
    private BigDecimal payTotalPrice;

    //发票id
    private String invoiceId;

    //支付方式
    private String payType;

    //支付渠道 PC M
    private String channel;

    //支付参数
    private Online online;

    public static class Online {

        //收货地址 收货人信息
        private Address address;

        //snapshotId - num       快照id 对应的商品数量
        private Map<String, Integer> snapshotCountMap;

        //兑换卡列表
        private List<String> cardList;

        //非前段传递参数，匹配参数
        private List<SnapshotInfo> snapshotInfoList;

        //快照商品总价
        private BigDecimal snapshotTotalPrice;

        //折扣总价
        private BigDecimal discountTotalPrice;

        //快递费
        private BigDecimal deliveryFee;

        //支付总价
        private BigDecimal payTotalPrice;

        //修理厂id
        private String serviceProviderId;

        //服务费 如果serviceProviderId没有，服务费一定为0
        private BigDecimal serviceFee;

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Map<String, Integer> getSnapshotCountMap() {
            return snapshotCountMap;
        }

        public void setSnapshotCountMap(Map<String, Integer> snapshotCountMap) {
            this.snapshotCountMap = snapshotCountMap;
        }

        public List<String> getCardList() {
            return cardList;
        }

        public void setCardList(List<String> cardList) {
            this.cardList = cardList;
        }

        public List<SnapshotInfo> getSnapshotInfoList() {
            return snapshotInfoList;
        }

        public void setSnapshotInfoList(List<SnapshotInfo> snapshotInfoList) {
            this.snapshotInfoList = snapshotInfoList;
        }

        public BigDecimal getSnapshotTotalPrice() {
            return snapshotTotalPrice;
        }

        public void setSnapshotTotalPrice(BigDecimal snapshotTotalPrice) {
            this.snapshotTotalPrice = snapshotTotalPrice;
        }

        public BigDecimal getDiscountTotalPrice() {
            return discountTotalPrice;
        }

        public void setDiscountTotalPrice(BigDecimal discountTotalPrice) {
            this.discountTotalPrice = discountTotalPrice;
        }

        public BigDecimal getDeliveryFee() {
            return deliveryFee;
        }

        public void setDeliveryFee(BigDecimal deliveryFee) {
            this.deliveryFee = deliveryFee;
        }

        public BigDecimal getPayTotalPrice() {
            return payTotalPrice;
        }

        public void setPayTotalPrice(BigDecimal payTotalPrice) {
            this.payTotalPrice = payTotalPrice;
        }

        public String getServiceProviderId() {
            return serviceProviderId;
        }

        public void setServiceProviderId(String serviceProviderId) {
            this.serviceProviderId = serviceProviderId;
        }

        public BigDecimal getServiceFee() {
            return serviceFee;
        }

        public void setServiceFee(BigDecimal serviceFee) {
            this.serviceFee = serviceFee;
        }
    }

    public BigDecimal getPayTotalPrice() {
        return payTotalPrice;
    }

    public void setPayTotalPrice(BigDecimal payTotalPrice) {
        this.payTotalPrice = payTotalPrice;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Online getOnline() {
        return online;
    }

    public void setOnline(Online online) {
        this.online = online;
    }


    public static class SnapshotInfo {

        //商品快照id
        private String goodsSnapshotId;

        //数量
        private Integer num;

        //匹配的兑换卡的信息
        private ArrayList<String> cardList;

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

        public ArrayList<String> getCardList() {
            return cardList;
        }

        public void setCardList(ArrayList<String> cardList) {
            this.cardList = cardList;
        }
    }
}



