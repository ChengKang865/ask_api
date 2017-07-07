DROP TABLE IF EXISTS `merchant_share_apply`;

CREATE TABLE `merchant_share_apply` (
  `id`            BIGINT(11)     NOT NULL  AUTO_INCREMENT
  COMMENT '物理主键',
  `apply_id`      VARCHAR(32)    NOT NULL
  COMMENT '提款申请ID',
  `merchant_id`   VARCHAR(32)    NOT NULL
  COMMENT '商户ID',
  `merchant_type` VARCHAR(32)    NOT NULL
  COMMENT '商户类型：service_provider 修理厂 partner 代理商 factory 合作工厂 mechanic 修理工 outlets  分销点',
  `amount`        DECIMAL(12, 2) NOT NULL
  COMMENT '申请提取金额',
  `fee`           DECIMAL(12, 2) NOT NULL
  COMMENT '手续费用',
  `status`        VARCHAR(20)    NOT NULL
  COMMENT '提取金额处理状态, doing:待处理, success:处理成功, failure:处理失败',
  `account`       VARCHAR(32)    NOT NULL
  COMMENT '收款账户',
  `account_name`  VARCHAR(32)    NOT NULL
  COMMENT '收款账户人姓名',
  `batch_no`      VARCHAR(32)
  COMMENT '支付批号',
  `serial_no`     VARCHAR(32)
  COMMENT '支付流水号',
  `remark`        VARCHAR(200)
  COMMENT '支付备注信息',
  `create_time`   TIMESTAMP      NOT NULL
  COMMENT '申请提交时间',
  `update_time`   TIMESTAMP      NOT NULL  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新提交时间',
  PRIMARY KEY (`id`),
  KEY index_merchant_id(`merchant_id`),
  KEY index_create_time(`create_time` DESC),
  UNIQUE KEY key_apply_id(`apply_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商户提取申请记录表';
  
  ALTER TABLE merchant_share_apply ADD INDEX idx_0 (update_time DESC );
  

ALTER TABLE merchant_share_apply
  MODIFY COLUMN update_time TIMESTAMP NULL ;


