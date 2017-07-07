# 商品
DROP TABLE IF EXISTS goods;
CREATE TABLE goods (
  id                BIGINT(11)     NOT NULL AUTO_INCREMENT
  COMMENT '物理主键',
  goods_id          VARCHAR(32)    NOT NULL
  COMMENT '商品的逻辑主键',
  goods_snapshot_id VARCHAR(32)             DEFAULT NULL
  COMMENT '商品快照id 当前商品的快照id',
  product_id        VARCHAR(32)    NOT NULL
  COMMENT '产品种类',
  name              VARCHAR(128)   NOT NULL
  COMMENT '商品名称',
  name_en           VARCHAR(128)   NOT NULL
  COMMENT '商品英文名称',
  popular_rank      INT(5)         NOT NULL DEFAULT 0
  COMMENT '流行等级 废弃不用',
  online_price      DECIMAL(10, 2)
  COMMENT '线上价格 线上价格跟线下价格统一',
  offline_price     DECIMAL(10, 2)
  COMMENT '线下服务价格',
  type              VARCHAR(32)    NOT NULL
  COMMENT '商品类型：all 线上线下 offline 线下 online 线上 offline废弃不用 o2o 对应 all f2c 对应 online 如果是online 必须制定 factoryId',
  status            VARCHAR(32)    NOT NULL
  COMMENT '商品状态：to_check 待审核 checked 审核通过 failure 失败',
  factory_id        VARCHAR(32)    NOT NULL DEFAULT ''
  COMMENT '代理工厂id',
  factory_fee       DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '代理工厂分成比例',
  agency_fee        DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '代理商分成比例',
  ad_fee            DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '广告分成',
  service_fee       DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '修理厂修理分成比例',
  handle_fee        DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '修理工分成',
  promote_fee       DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '推广注册分成',
  create_time       TIMESTAMP      NOT NULL
  COMMENT '创建时间',
  creator_id        VARCHAR(32)    NOT NULL
  COMMENT '创建人id',
  modify_time       TIMESTAMP
  COMMENT '修改时间',
  modify_id         VARCHAR(32)
  COMMENT '修改人id',
  check_time        TIMESTAMP      NOT NULL
  COMMENT '校验时间',
  check_id          VARCHAR(32)    NOT NULL
  COMMENT '校验人id',
  delivery_fee      DECIMAL(6, 3)  NOT NULL DEFAULT 0.000
  COMMENT '快递价格',
  delete_flag       BOOLEAN        NOT NULL DEFAULT FALSE
  COMMENT '删除标志位',
  sale_flag         BOOLEAN        NOT NULL DEFAULT FALSE
  COMMENT '在售状态位',
  check_snapshot_id VARCHAR(32)
  COMMENT '检查审核的商品id',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (goods_id),
  INDEX index_0 (product_id),
  INDEX index_1 (factory_id),
  INDEX index_2 (create_time DESC),
  INDEX index_3 (creator_id),
  INDEX index_4 (modify_time DESC),
  INDEX index_5 (modify_id),
  INDEX index_6 (check_time DESC),
  INDEX index_7 (check_id),
  INDEX index_8 (goods_snapshot_id),
  INDEX index_9 (check_snapshot_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商品表';
