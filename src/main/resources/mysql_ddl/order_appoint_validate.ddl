DROP TABLE IF EXISTS order_appoint_validate;

CREATE TABLE `order_appoint_validate` (
  `id`                  BIGINT(11)  NOT NULL AUTO_INCREMENT
  COMMENT '物理主键',
  `order_id`            VARCHAR(32) NOT NULL
  COMMENT '逻辑主键',
  `user_id`             VARCHAR(32) NOT NULL
  COMMENT '用户主键',
  `user_phone`          VARCHAR(32) NOT NULL
  COMMENT '用户手机号码',
  `service_provider_id` VARCHAR(32) NOT NULL
  COMMENT '服务点id',
  `code`                VARCHAR(6)  NOT NULL
  COMMENT '验证码',
  `validate_flag`       TINYINT(1)  NOT NULL DEFAULT '0'
  COMMENT '验证标识位,0 未验证 1 已验证',
  `create_time`         TIMESTAMP   NOT NULL
  COMMENT '创建时间',
  `validate_time`       TIMESTAMP   NULL
  COMMENT '验证时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_index_0` (`order_id`),
  KEY `index_0` (`user_id`),
  KEY `index_1` (`service_provider_id`, `code`, `validate_flag`),
  KEY `index_2` (`create_time`),
  KEY `index_4` (`validate_time`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '订单验证码';