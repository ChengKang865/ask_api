# 订单商品中间表
DROP TABLE IF EXISTS order_goods;
CREATE TABLE order_goods
(
  id                BIGINT(11)     NOT NULL AUTO_INCREMENT
  COMMENT '物理主键',
  order_goods_id    VARCHAR(32)    NOT NULL
  COMMENT '逻辑主键',
  order_id          VARCHAR(32)    NOT NULL
  COMMENT '订单逻辑主键',
  goods_snapshot_id VARCHAR(32)    NOT NULL
  COMMENT '商品快照uuid',
  num               INT            NOT NULL
  COMMENT '商品数量',
  snapshot_price    DECIMAL(10, 2) NOT NULL
  COMMENT '快照总价',
  discount_price    DECIMAL(10, 2) NOT NULL
  COMMENT '折扣价格',
  pay_price         DECIMAL(10, 2) NOT NULL
  COMMENT '实际付款价格',
  PRIMARY KEY (id),
  UNIQUE INDEX u_idx_0 (order_goods_id),
  INDEX idx_0 (order_id),
  INDEX idx_3 (goods_snapshot_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '订单商品中间表';