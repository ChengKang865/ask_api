# 卡 现金卡 产品兑换卡 产品现金兑换卡
DROP TABLE IF EXISTS order_goods_card;
CREATE TABLE order_goods_card (
  id             BIGINT(11)  NOT NULL        AUTO_INCREMENT
  COMMENT '物理主键',
  order_goods_id VARCHAR(32) NOT NULL
  COMMENT '对应order_goods中的order_goods_id',
  card_id        VARCHAR(32) NOT NULL
  COMMENT 'card_id',
  card_type_id   VARCHAR(32) NOT NULL
  COMMENT '冗余字段 为了查询方便',
  PRIMARY KEY (id),
  UNIQUE INDEX u_idx_0 (order_goods_id, card_id),
  INDEX idx_0 (card_id),
  INDEX idx_1 (card_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '订单商品对应的卡表';