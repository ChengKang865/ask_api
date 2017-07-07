# 商品快照
DROP TABLE IF EXISTS goods_snapshot;
CREATE TABLE goods_snapshot (
  id                    BIGINT(11)     NOT NULL AUTO_INCREMENT
  COMMENT '物理主键',
  goods_snapshot_id     VARCHAR(32)    NOT NULL
  COMMENT '逻辑主键',
  goods_id              VARCHAR(32)
  COMMENT '商品的逻辑主键 如果这个是新增goods goods_id 为null 审核通过了生成goods_id',
  product_id            VARCHAR(32)    NOT NULL
  COMMENT '产品种类',
  name                  VARCHAR(128)   NOT NULL
  COMMENT '商品名称',
  name_en               VARCHAR(128)   NOT NULL
  COMMENT '商品英文名称',
  popular_rank          INT(5)         NOT NULL DEFAULT 0
  COMMENT '流行等级',
  online_price          DECIMAL(10, 2)
  COMMENT '线上价格',
  offline_price         DECIMAL(10, 2)
  COMMENT '线下服务价格',
  type                  VARCHAR(32)    NOT NULL
  COMMENT '商品类型：all 线上线下 offline 线下 online 线上',
  status                VARCHAR(32)    NOT NULL
  COMMENT '商品状态：to_check 待审核 checked 审核通过 failure 失败',
  factory_id            VARCHAR(32)    NOT NULL DEFAULT ''
  COMMENT '代理工厂id',
  factory_fee           DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '代理工厂分成比例',
  agency_fee            DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '代理商分成比例',
  ad_fee                DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '广告分成',
  service_fee           DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '修理厂修理分成比例',
  handle_fee            DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '修理工分成',
  promote_fee           DECIMAL(10, 2) NOT NULL DEFAULT 0.000
  COMMENT '推广注册分成',
  create_time           TIMESTAMP      NOT NULL
  COMMENT '创建时间',
  creator_id            VARCHAR(32)    NOT NULL
  COMMENT '创建人id',
  modify_time           TIMESTAMP
  COMMENT '修改时间',
  modify_id             VARCHAR(32)
  COMMENT '修改人id',
  check_time            TIMESTAMP      NOT NULL
  COMMENT '校验时间',
  check_id              VARCHAR(32)    NOT NULL
  COMMENT '校验人id',
  delivery_fee          DECIMAL(6, 3)  NOT NULL DEFAULT 0.000
  COMMENT '快递价格',
  delete_flag           BOOLEAN        NOT NULL DEFAULT FALSE
  COMMENT '删除标志位',
  snapshot_check_status VARCHAR(32)    NOT NULL DEFAULT 'checked'
  COMMENT 'checking to_check,checked   checked 是验证过 to_check状态的不能作为商品的主键',
  snapshot_create_time  TIMESTAMP      NOT NULL
  COMMENT '快照的创建时间',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_0 (goods_snapshot_id),
  INDEX index_0 (goods_id),
  INDEX index_1 (snapshot_create_time)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '商品快照表';