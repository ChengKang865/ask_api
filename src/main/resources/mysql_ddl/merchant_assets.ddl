# 商户资产表
DROP TABLE IF EXISTS merchant_assets;
CREATE TABLE merchant_assets (
  id            BIGINT(11)     NOT NULL AUTO_INCREMENT,
  merchant_type VARCHAR(32)    NOT NULL
  COMMENT '商户类型：service_provider 修理厂 partner 代理商 factory 合作工厂 mechanic 修理工 outlets  分销点',
  merchant_id   VARCHAR(32)    NOT NULL
  COMMENT 'service_provider 对应ServiceProvider id 以此类推 全存放在Mongodb中',
  balance       DECIMAL(10, 2) NOT NULL
  COMMENT '商户余额',
  income_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00
  COMMENT '收入总额',
  create_time   TIMESTAMP      NOT NULL
  COMMENT '创建时间',
  modify_time   TIMESTAMP      NOT NULL
  COMMENT '修改时间',
  version       BIGINT(11)     NOT NULL DEFAULT 0
  COMMENT '版本号',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (merchant_id, merchant_type),
  INDEX index_0 (create_time DESC),
  INDEX index_1 (modify_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商户资产表';

