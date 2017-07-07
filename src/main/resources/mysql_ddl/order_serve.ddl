# 订单线下服务表
DROP TABLE IF EXISTS order_serve;
CREATE TABLE order_serve
(
  id                         BIGINT(11)  NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  order_id                   VARCHAR(32) NOT NULL
  COMMENT '订单逻辑主键',
  mechanic_id                VARCHAR(32)
  COMMENT '修理工主键 存放在Mongodb中',
  service_provider_id        VARCHAR(32) NOT NULL
  COMMENT '修理厂uuid',
  service_provider_pre_share DECIMAL(10, 2)
  COMMENT '修理厂预计分成',
  partner_id                 VARCHAR(32) NOT NULL
  COMMENT '代理商id',
  partner_pre_share          DECIMAL(10, 2)
  COMMENT '合作商预计分成',
  check_merchant_type        VARCHAR(32) NOT NULL
  COMMENT '检查方，只有当order为offline_appoint 时候才会是 partner 或者 autoask 以此标注缺货状态',
  rate                       SMALLINT
  COMMENT '用户评价等级',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (order_id),
  INDEX index_0 (mechanic_id),
  INDEX index_1 (service_provider_id),
  INDEX index_2 (partner_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '线下服务表';