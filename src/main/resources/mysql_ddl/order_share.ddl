# order_goods_share 订单商品分成记录
DROP TABLE IF EXISTS order_share;
CREATE TABLE order_share (
  id           BIGINT(11)  NOT NULL  AUTO_INCREMENT
  COMMENT '逻辑主键',
  order_id     VARCHAR(32) NOT NULL
  COMMENT '关联 order_goods 逻辑主键',
  ad_fee       DECIMAL(10, 2)
  COMMENT '广告费',
  ad_type      VARCHAR(64)
  COMMENT '广告费方 两种：修理厂 和 合作商',
  ad_id        VARCHAR(32)
  COMMENT '修理厂id或者合作商id',
  service_fee  DECIMAL(10, 2)
  COMMENT '修理厂手工费 分给修理厂 获取 合作商',
  service_type VARCHAR(32)
  COMMENT '修理费分成方:可能是 partner 当只有寄送的时候',
  service_id   VARCHAR(32)
  COMMENT '修理厂id 或者 partner_id',
  handle_fee   DECIMAL(10, 2)
  COMMENT '修理工处理费',
  handle_id    VARCHAR(32)
  COMMENT '修理工id',
  promote_fee  DECIMAL(10, 2)
  COMMENT '引流费',
  promote_type VARCHAR(64)
  COMMENT '引流方类型：代理商 修理厂 分销点 个人引流或者没有引流为null',
  promote_id   VARCHAR(32)
  COMMENT '对应引流方id',
  factory_fee  DECIMAL(10, 2)
  COMMENT '代理工厂分成',
  factory_id   VARCHAR(32)
  COMMENT '代理工厂id',
  partner_fee  DECIMAL(10, 2)
  COMMENT '代理商分成',
  partner_id   VARCHAR(32)
  COMMENT '代理商id',
  create_time  TIMESTAMP   NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY u_idx_0 (order_id),
  INDEX idx_0 (ad_id, ad_type),
  INDEX idx_1 (service_id, service_type),
  INDEX idx_2  (handle_id),
  INDEX idx_3 (promote_id, promote_type),
  INDEX idx_4 (factory_id),
  INDEX idx_5 (partner_id),
  INDEX idx_6 (create_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'order对应的分成';