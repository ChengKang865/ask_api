package com.autoask.entity.mysql;

import javax.persistence.*;

/**
 * Created by hyy on 2016/12/6.
 */
@Entity
@Table
public class OrderGoodsCard {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String orderGoodsId;

    private String cardId;

    /**
     * 冗余信息，主要为了查询更少的left join
     */
    private String cardTypeId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(String orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }
}
