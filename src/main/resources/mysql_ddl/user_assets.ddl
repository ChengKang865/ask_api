# 用户资产表
DROP TABLE IF EXISTS user_assets;
CREATE TABLE `user_assets` (
  `id`          BIGINT(11)     NOT NULL AUTO_INCREMENT
  COMMENT '积分记录id号',
  `user_id`     VARCHAR(32)    NOT NULL
  COMMENT '用户id号',
  `integration` BIGINT(11)     NOT NULL DEFAULT '0'
  COMMENT '积分表',
  `balance`     DECIMAL(11, 2) NOT NULL DEFAULT '0.00'
  COMMENT '用户当前余额',
  `create_time` TIMESTAMP      NOT NULL
  COMMENT '记录创建时间',
  `modify_time` TIMESTAMP      NOT NULL
  COMMENT '记录修改时间',
  `version`     BIGINT(11)
  COMMENT '记录修改时间',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (user_id),
  INDEX index_0 (create_time DESC),
  INDEX index_1 (modify_time DESC)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '用户资产表';