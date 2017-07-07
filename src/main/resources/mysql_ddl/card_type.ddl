# 卡类型表
DROP TABLE IF EXISTS card_type;
CREATE TABLE card_type (
  id             BIGINT(11)    NOT NULL        AUTO_INCREMENT
  COMMENT '物理主键',
  card_type_id   VARCHAR(32)   NOT NULL
  COMMENT '逻辑主键 批次号 生成规则是时间字符串',
  name           VARCHAR(128)  NOT NULL
  COMMENT '名称',
  num            BIGINT(11)    NOT NULL
  COMMENT '初始数量',
  status         VARCHAR(32)   NOT NULL
  COMMENT '状态  to_check check expired to_check 等待激活 check 激活 作废就是deleteFlag',
  factory_fee   DECIMAL(10, 2) NOT NULL
  COMMENT '代理工厂分成比例',
  `agency_fee`  DECIMAL(10, 2) NOT NULL
  COMMENT '代理商分成比例',
  `ad_fee`      DECIMAL(10, 2) NOT NULL
  COMMENT '广告分成',
  `service_fee` DECIMAL(10, 2) NOT NULL
  COMMENT '修理厂修理分成比例',
  `handle_fee`  DECIMAL(10, 2) NOT NULL
  COMMENT '修理工分成',
  `promote_fee` DECIMAL(10, 2) NOT NULL
  COMMENT '推广注册分成',
  delete_flag    TINYINT(1)    NOT NULL        DEFAULT 0
  COMMENT '删除标志位',
  create_time    TIMESTAMP     NOT NULL
  COMMENT '创建时间',
  expire_time    TIMESTAMP     NOT NULL
  COMMENT '失效时间',
  creator_id     VARCHAR(32)   NOT NULL
  COMMENT '创建人id',
  modify_time    TIMESTAMP NULL
  COMMENT '修改时间',
  modify_id      VARCHAR(32)
  COMMENT '修改人id',
  KEY (id),
  UNIQUE KEY u_idx_0 (card_type_id),
  KEY idx_0 (name),
  KEY idx_1 (create_time DESC),
  KEY idx_2 (creator_id),
  KEY idx_3 (modify_time DESC),
  KEY idx_4 (modify_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '卡类型表';