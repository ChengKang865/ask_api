# 用户表
DROP TABLE IF EXISTS user;
CREATE TABLE user (
  id                BIGINT(11)  NOT NULL        AUTO_INCREMENT,
  user_id           VARCHAR(32) NOT NULL
  COMMENT '用户逻辑主键',
  phone             VARCHAR(11) NOT NULL
  COMMENT '用户手机号码',
  password          VARCHAR(64)                 DEFAULT NULL
  COMMENT '用户密码，暂时不需要密码登录',
  recommend_user_id VARCHAR(32)                 DEFAULT NULL
  COMMENT '推荐人id,可以为空',
  recommend_phone   VARCHAR(32)                 DEFAULT NULL
  COMMENT '推荐人手机号码',
  promote_id        VARCHAR(32)                 DEFAULT NULL
  COMMENT '引流方id',
  promote_type      VARCHAR(64)                 DEFAULT NULL
  COMMENT '引流方type:修理厂 分销点 代理商',
  name              VARCHAR(64)
  COMMENT '用户姓名',
  province          VARCHAR(64)
  COMMENT '省份',
  city              VARCHAR(64)
  COMMENT '城市',
  region            VARCHAR(128)
  COMMENT '街道',
  detail_address    VARCHAR(256)
  COMMENT '详细地址',
  sex               SMALLINT(1)
  COMMENT '性别',
  delete_flag       BOOLEAN
  COMMENT '删除标志位 true 为 1 是 false 为否 0',
  wx_open_id        VARCHAR(64)
  COMMENT '微信的open_id',
  create_time       TIMESTAMP   NOT NULL        DEFAULT current_timestamp
  COMMENT '生成时间',
  modify_time       TIMESTAMP
  COMMENT '修改时间',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (user_id),
  UNIQUE INDEX u_index_1 (phone),
  INDEX index_0 (recommend_user_id),
  INDEX index_1 (recommend_phone),
  INDEX index_2 (promote_id, promote_type),
  INDEX index_3 (name),
  INDEX index_4 (create_time DESC),
  INDEX index_5 (modify_time DESC),
  INDEX index_6 (wx_open_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '用户基础表';