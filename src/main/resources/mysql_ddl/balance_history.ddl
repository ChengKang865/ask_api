# 用户余额变化表
DROP TABLE IF EXISTS balance_history;
CREATE TABLE `balance_history` (
  id                 BIGINT(11)     NOT NULL AUTO_INCREMENT
  COMMENT '物理主键',
  balance_history_id VARCHAR(32)    NOT NULL
  COMMENT '逻辑主键',
  user_id            VARCHAR(32)    NOT NULL
  COMMENT '用户id号',
  pre_amount         DECIMAL(10, 2) NOT NULL
  COMMENT '上次额度',
  current_amount     DECIMAL(10, 2) NOT NULL
  COMMENT '变化后额度',
  change_amount      DECIMAL(10, 2) NOT NULL
  COMMENT '变化额度 减少为负数 增加为正数 此字段冗余 为了查询方便',
  type               VARCHAR(16)             DEFAULT 'decrease'
  COMMENT '余额变化类型: discount 抵扣订单，消费了余额  card 全品类抵扣卡, promote 引流了用户',
  extra_id           VARCHAR(32)    NOT NULL
  COMMENT '关联id  1.type为discount时关联order_id 2.type为card时关联car_id car是一个全品类的卡,数据存放在Mongodb中 3.reward 可能为消费返现,此业务逻辑暂不做考虑 4. type是promote的时候是对应order_id',
  create_time        TIMESTAMP      NULL     DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (balance_history_id),
  INDEX index_0 (user_id),
  INDEX index_1 (extra_id),
  INDEX index_2 (create_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '用户余额变化记录表';