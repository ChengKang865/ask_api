DROP TABLE IF EXISTS goods_num_record;
CREATE TABLE goods_num_record (
  id            BIGINT(11)  NOT NULL AUTO_INCREMENT
  COMMENT '物理主键',
  record_id     VARCHAR(32) NOT NULL
  COMMENT '逻辑主键',
  goods_id      VARCHAR(32) NOT NULL
  COMMENT '商品id',
  merchant_type VARCHAR(32) NOT NULL
  COMMENT '商户类型',
  merchant_id   VARCHAR(32)
  COMMENT '商户id',
  change_num    BIGINT(11)      NOT NULL
  COMMENT '商品数量',
  pre_num       BIGINT(11)      NOT NULL
  COMMENT '更改之前的数量',
  create_time   TIMESTAMP   NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY u_index_0 (record_id),
  INDEX index_0 (goods_id),
  INDEX index_1 (merchant_id, merchant_type),
  INDEX index_2(create_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商品数量变更表';