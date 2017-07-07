# 商户资产入账记录
DROP TABLE IF EXISTS assets_record_item;
CREATE TABLE assets_record_item (
  id             BIGINT(11)     NOT NULL AUTO_INCREMENT,
  record_item_id VARCHAR(32)    NOT NULL
  COMMENT '逻辑主键',
  record_id      VARCHAR(32)    NOT NULL,
  amount         DECIMAL(10, 2) NOT NULL
  COMMENT '改变的额度 冗余字段  负数为减少 正数为增加',
  income_type    VARCHAR(32)    NOT NULL
  COMMENT '收入类型 各种分成的定义',
  PRIMARY KEY (id),
  UNIQUE KEY u_index_0 (record_item_id),
  INDEX index_0 (record_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商户资产变化表';