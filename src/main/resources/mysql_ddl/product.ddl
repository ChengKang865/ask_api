DROP TABLE IF EXISTS product;
CREATE TABLE product (
  id                  BIGINT(11)   NOT NULL AUTO_INCREMENT
  COMMENT '物理主键',
  product_id          VARCHAR(32)  NOT NULL
  COMMENT '产品的逻辑主键',
  product_category_id VARCHAR(32)  NOT NULL
  COMMENT '所属产品类别逻辑主键',
  name                VARCHAR(128) NOT NULL
  COMMENT '产品名称 唯一',
  name_en             VARCHAR(128) NOT NULL
  COMMENT '产品英文名称 唯一',
  description         VARCHAR(256) NOT NULL
  COMMENT '产品描述',
  logo_url            VARCHAR(128) NOT NULL
  COMMENT '产品logo url',
  delete_flag         BOOLEAN      NOT NULL DEFAULT FALSE
  COMMENT '删除标志位',
  create_time         TIMESTAMP    NOT NULL
  COMMENT '创建时间',
  creator_id          VARCHAR(32)  NOT NULL
  COMMENT '创建人id',
  modify_time         TIMESTAMP
  COMMENT '修改时间',
  modify_id           VARCHAR(32)
  COMMENT '修改人id',
  PRIMARY KEY (id),
  --   UNIQUE INDEX u_index_0 (name),
  --   UNIQUE INDEX u_index_1 (name_en),
  UNIQUE INDEX u_index_2 (product_id),
  INDEX index_0 (create_time DESC),
  INDEX index_1 (creator_id),
  INDEX index_2 (modify_time DESC),
  INDEX index_3 (modify_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '产品';


ALTER TABLE product ADD COLUMN head_str VARCHAR(64) NOT NULL DEFAULT ''
COMMENT 'pc首页产品head 名称';