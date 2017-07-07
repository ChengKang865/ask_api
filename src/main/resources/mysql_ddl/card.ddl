# 卡 现金卡 产品兑换卡 产品现金兑换卡
DROP TABLE IF EXISTS card;
CREATE TABLE card (
  id           BIGINT(11)  NOT NULL        AUTO_INCREMENT
  COMMENT '物理主键',
  card_id      VARCHAR(32) NOT NULL
  COMMENT '序列号 为逻辑主键',
  verify_code  VARCHAR(32)                 DEFAULT NULL
  COMMENT '卡的校验码 暂时废弃不用',
  status       VARCHAR(32) NOT NULL
  COMMENT '卡的状态 un_check checked to_use used',
  card_type_id VARCHAR(32) NOT NULL
  COMMENT '卡类型主键',
  expire_time  TIMESTAMP   NOT NULL
  COMMENT '失效时间',
  use_time     TIMESTAMP NULL 
  COMMENT '使用时间',
  user_id      VARCHAR(32)
  COMMENT '使用人id',
  check_time   TIMESTAMP NULL 
  COMMENT '批注时间',
  check_id     VARCHAR(32)
  COMMENT '批准人id',
  delete_flag  TINYINT(1)  NOT NULL        DEFAULT 0
  COMMENT '删除标志位置',
  PRIMARY KEY (id),
  UNIQUE INDEX u_idx_0 (card_id),
  INDEX idx_1 (card_type_id),
  INDEX idx_2 (expire_time DESC),
  INDEX idx_3 (use_time DESC),
  INDEX idx_5 (user_id),
  INDEX idx_8 (check_id),
  INDEX idx_9 (check_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '卡表';

ALTER TABLE card DROP INDEX idx_3;

ALTER TABLE card DROP COLUMN expire_time;

ALTER TABLE card ADD COLUMN type_sort BIGINT(11) DEFAULT 0 COMMENT '卡的排列序号';

ALTER TABLE card
  ADD COLUMN delete_time TIMESTAMP NULL
COMMENT '删除时间';

ALTER TABLE card
    ADD COLUMN delete_id VARCHAR(32) DEFAULT NULL
COMMENT '删除人员id';

ALTER TABLE card
    ADD COLUMN order_goods_id VARCHAR(32) DEFAULT NULL
COMMENT '关联的订单商品id';

ALTER TABLE card
  ADD INDEX idx_4 (order_goods_id);

