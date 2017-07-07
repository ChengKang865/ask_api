DROP TABLE IF EXISTS goods_num;
CREATE TABLE `goods_num` (
  id            BIGINT(11)  NOT NULL AUTO_INCREMENT
  COMMENT '物理主键',
  goods_id      VARCHAR(32) NOT NULL
  COMMENT '商品id',
  merchant_type VARCHAR(32) NOT NULL
  COMMENT '商户类型',
  merchant_id   VARCHAR(32)
  COMMENT '商户id',
  num           BIGINT      NOT NULL
  COMMENT '商品数量',
  create_time   TIMESTAMP   NOT NULL
  COMMENT '创建时间',
  modify_time   TIMESTAMP   NOT NULL
  COMMENT '修改时间',
  PRIMARY KEY (id),
  UNIQUE KEY `u_index_0` (goods_id, merchant_id, merchant_type),
  INDEX index_0 (goods_id),
  INDEX index_1 (merchant_id, merchant_type),
  INDEX index_2(create_time DESC),
  INDEX index_3 (modify_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商品数量表';