DROP TABLE IF EXISTS goods_info;
CREATE TABLE goods_info (
  id            BIGINT(11)   NOT NULL AUTO_INCREMENT
  COMMENT '物理主键',
  goods_info_id VARCHAR(32)  NOT NULL
  COMMENT '逻辑主键',
  goods_id      VARCHAR(32)  NOT NULL
  COMMENT 'goods_id',
  key_name      VARCHAR(128) NOT NULL
  COMMENT 'key',
  value         VARCHAR(128) NOT NULL
  COMMENT 'value',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (goods_info_id),
  INDEX index_0 (goods_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商品详情';