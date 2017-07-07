DROP TABLE IF EXISTS order_delivery;
CREATE TABLE order_delivery
(
  id              BIGINT(11)  NOT NULL        AUTO_INCREMENT
  COMMENT '主键',
  order_id        VARCHAR(32) NOT NULL
  COMMENT '订单逻辑主键',
  merchant_type   VARCHAR(32) NOT NULL
  COMMENT '发货商户类型',
  merchant_id     VARCHAR(32)
  COMMENT '商户id',
  pre_share       DECIMAL(10, 2)
  COMMENT '预计分成',
  receiver_name   VARCHAR(256)
  COMMENT '收货人 姓名',
  receiver_phone  VARCHAR(20)
  COMMENT '收货人 手机号码',
  province        VARCHAR(16) NOT NULL
  COMMENT '收货地址(省)',
  city            VARCHAR(16) NOT NULL
  COMMENT '收货地址(市)',
  region          VARCHAR(16) NOT NULL
  COMMENT '收货地址(区)',
  street          VARCHAR(32) NOT NULL
  COMMENT '收货地址(街道)',
  detail_address  VARCHAR(64) NOT NULL
  COMMENT '详细收货地址',
  express_company VARCHAR(32)
  COMMENT '物流公司(枚举)',
  delivery_serial VARCHAR(32)
  COMMENT '物流编号',
  delivery_time   TIMESTAMP NULL 
  COMMENT '发货时间',
  PRIMARY KEY (id),
  UNIQUE INDEX u_idx_0 (order_id),
  INDEX idx_0 (merchant_id, merchant_type),
  INDEX idx_1 (delivery_serial),
  INDEX idx_2 (delivery_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '订单快递表';