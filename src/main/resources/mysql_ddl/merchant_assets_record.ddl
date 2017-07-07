# 商户资产入账记录
DROP TABLE IF EXISTS merchant_assets_record;
CREATE TABLE merchant_assets_record (
  id            BIGINT(11)     NOT NULL AUTO_INCREMENT,
  record_id     VARCHAR(32)    NOT NULL
  COMMENT '逻辑主键',
  merchant_type VARCHAR(32)    NOT NULL
  COMMENT '商户类型：service_provider 修理厂 partner 代理商 factory 合作工厂 mechanic 修理工 outlets  分销点',
  merchant_id   VARCHAR(32)    NOT NULL
  COMMENT 'service_provider 对应ServiceProvider id 以此类推 全存放在Mongodb中',
  change_amount DECIMAL(10, 2) NOT NULL
  COMMENT '改变的额度 冗余字段  负数为减少 正数为增加',
  related_type  VARCHAR(32)    NOT NULL
  COMMENT '关联的收入类型 order_delivery order_serve',
  related_id    VARCHAR(32)    NOT NULL
  COMMENT '关联的收入订单id',
  create_time   TIMESTAMP      NOT NULL
  COMMENT '生成时间',
  PRIMARY KEY (id),
  UNIQUE KEY u_index_0 (record_id),
  INDEX index_0 (merchant_id, merchant_type),
  INDEX index_1 (related_id, related_type),
  INDEX index_2 (create_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商户资产变化表';