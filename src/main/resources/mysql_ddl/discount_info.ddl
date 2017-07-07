# 减免信息表
# order_info discount_info   1:n
DROP TABLE IF EXISTS discount_info;
CREATE TABLE discount_info (
  id               BIGINT(11)  NOT NULL AUTO_INCREMENT,
  discount_info_id VARCHAR(32) NOT NULL
  COMMENT '减免信息逻辑主键',
  order_id         VARCHAR(32) NOT NULL
  COMMENT '订单逻辑主键 关联order表的order_id',
  type             SMALLINT    NOT NULL
  COMMENT '减免类型 1 积分 2 卡 3 余额减免',
  amount           DECIMAL(10, 2)
  COMMENT '减免额度',
  extra_id         VARCHAR(32)
  COMMENT '积分对应积分变化表的逻辑主键 余额对应余额变化表的逻辑主键 卡对应着卡的主键 卡存放在Mongodb中',
  extra_info       VARCHAR(128)
  COMMENT '',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (discount_info_id),
  INDEX index_0 (order_id),
  INDEX index_1 (extra_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '减免信息表';