# 订单表
DROP TABLE IF EXISTS order_info;
CREATE TABLE order_info (
  id               BIGINT(11)     NOT NULL        AUTO_INCREMENT,
  order_id         VARCHAR(32)    NOT NULL
  COMMENT '逻辑主键',
  serial           VARCHAR(32)    NOT NULL
  COMMENT '序列号，同一次下单的时候生成的是同一个serial',
  user_id          VARCHAR(32)    NOT NULL
  COMMENT '用户主键',
  snapshot_price   DECIMAL(10, 2) NOT NULL
  COMMENT '商品快照总价',
  delivery_fee     DECIMAL(10, 2) NOT NULL
  COMMENT '快递费用',
  discount_price   DECIMAL(10, 2) NOT NULL
  COMMENT '折扣价格',
  pay_price        DECIMAL(10, 2) NOT NULL
  COMMENT '真实付款总额',
  serve_type       VARCHAR(16)    NOT NULL
  COMMENT 'online offline offline_appoint offline_cash',
  status           VARCHAR(32)    NOT NULL
  COMMENT '订单状态',
  pay_type         VARCHAR(16)    NOT NULL
  COMMENT '支付方式 ali wx union_pay cash',
  pay_serial       VARCHAR(128)                   DEFAULT NULL
  COMMENT '支付序列号',
  pay_time         TIMESTAMP      NULL
  COMMENT '支付时间',
  refund_serial    VARCHAR(128)
  COMMENT '退款序列号',
  refund_type      VARCHAR(32)
  COMMENT '退款人类型',
  refund_id        VARCHAR(32)
  COMMENT '退款人id',
  refund_time      TIMESTAMP      NULL
  COMMENT '退款时间',
  create_time      TIMESTAMP      NOT NULL
  COMMENT '创建时间',
  receive_time     TIMESTAMP      NULL
  COMMENT '收货/确认服务 时间',
  comment_time     TIMESTAMP      NULL
  COMMENT '评价时间',
  user_delete_flag TINYINT(1)     NOT NULL        DEFAULT 0
  COMMENT '用户删除标志',
  invoice_id       VARCHAR(32)
  COMMENT '发票id',
  PRIMARY KEY (id),
  UNIQUE INDEX u_idx_0 (order_id),
  INDEX idx_0 (serial),
  INDEX idx_1 (user_id),
  INDEX idx_2 (pay_serial, pay_type),
  INDEX idx_3 (pay_time DESC),
  INDEX idx_4 (refund_serial),
  INDEX idx_5 (refund_id, refund_type),
  INDEX idx_6 (refund_time DESC),
  INDEX idx_7 (create_time DESC),
  INDEX idx_8 (receive_time DESC),
  INDEX idx_9 (comment_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '订单表';

ALTER TABLE order_info
  ADD COLUMN share_status VARCHAR(32) NOT NULL DEFAULT 'no_share';

ALTER TABLE order_info
    ADD COLUMN share_time TIMESTAMP NULL ;
ALTER TABLE order_info
  ADD INDEX idx_10 (share_time DESC);
ALTER TABLE order_info ADD COLUMN share_operator_id VARCHAR(32) ;
ALTER TABLE order_info
  ADD INDEX idx(share_operator_id);

ALTER TABLE order_info
    ADD COLUMN comment_rate SMALLINT DEFAULT 0;
