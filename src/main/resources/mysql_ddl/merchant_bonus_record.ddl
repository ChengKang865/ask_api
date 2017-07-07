# 商户红包ping++发送记录
DROP TABLE IF EXISTS merchant_bonus_record;
CREATE TABLE merchant_bonus_record (
  id                 BIGINT(11)     NOT NULL  AUTO_INCREMENT,
  record_id          VARCHAR(32)    NOT NULL
  COMMENT '逻辑主键',
  merchant_type      VARCHAR(32)    NOT NULL
  COMMENT '商户类型：mechanic 修理工 outlets  分销点',
  merchant_id        VARCHAR(32)    NOT NULL
  COMMENT '存放在Mongodb中',
  pre_amount         DECIMAL(10, 2) NOT NULL
  COMMENT '之前的额度',
  now_amount         DECIMAL(10, 2) NOT NULL
  COMMENT '当前的额度',
  change_amount      DECIMAL(10, 2) NOT NULL
  COMMENT '改变的额度 冗余字段  负数为减少 正数为增加',
  status             VARCHAR(32)    NOT NULL
  COMMENT '发送状态, doing:处理中，success:发送成功, failure:发送失败',
  pingpp_transfer_id VARCHAR(32)    NOT NULL
  COMMENT 'ping++返回的transferId',
  remark             VARCHAR(300)
  COMMENT '备注信息',
  create_time        TIMESTAMP      NOT NULL
  COMMENT '生成时间',
  `update_time`      TIMESTAMP      NOT NULL  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新提交时间',
  PRIMARY KEY (id),
  UNIQUE KEY u_index_0 (record_id),
  KEY u_index_1 (merchant_id, merchant_type),
  KEY index_pingpp_transfer_id(pingpp_transfer_id),
  INDEX index_1 (create_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商户红包ping++发送记录';