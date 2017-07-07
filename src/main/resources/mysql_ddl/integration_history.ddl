# 用户积分记录表
DROP TABLE IF EXISTS integration_history;
CREATE TABLE `integration_history` (
  id                     BIGINT(11)  NOT NULL AUTO_INCREMENT,
  integration_history_id VARCHAR(32) NOT NULL
  COMMENT '逻辑主键',
  user_id                VARCHAR(32) NOT NULL
  COMMENT '用户id 关联Mongodb中user的id',
  pre_integration        BIGINT(11)  NOT NULL DEFAULT '0'
  COMMENT '上一次的积分',
  current_integration    BIGINT(11)  NOT NULL DEFAULT '0'
  COMMENT '当前积分',
  change_amount          BIGINT(11)  NOT NULL
  COMMENT '积分变化额度，增加为正，减少为负，此字段冗余仅是为了查询时方便',
  type                   VARCHAR(16)
  COMMENT '积分变换的类型:discount(积分抵扣),reward(消费奖励)',
  extra_id               VARCHAR(32) NOT NULL
  COMMENT '对应具体订单id', #原先设计为 extra_id 当为discount 和 reward时都需要对应order_id 兑换商品时可能不对应order_id
  create_time            TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (integration_history_id),
  INDEX index_0 (user_id),
  INDEX index_1 (extra_id),
  INDEX index_2 (create_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '用户积分记录表';