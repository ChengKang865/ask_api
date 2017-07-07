# 产品种类
DROP TABLE IF EXISTS product_category;
CREATE TABLE product_category (
  id                  BIGINT(11)   NOT NULL        AUTO_INCREMENT
  COMMENT '物理主键',
  product_category_id VARCHAR(32)  NOT NULL,
  name                VARCHAR(128) NOT NULL
  COMMENT '产品种类名称 唯一',
  name_en             VARCHAR(128) NOT NULL
  COMMENT '产品种类英文简称 唯一',
  logo_url            VARCHAR(128)
  COMMENT '产品种类的logo地址',
  create_time         TIMESTAMP    NOT NULL
  COMMENT '创建时间',
  creator_id          VARCHAR(32)  NOT NULL
  COMMENT '创建者id',
  modify_time         TIMESTAMP
  COMMENT '修改时间',
  modify_id           VARCHAR(32)
  COMMENT '修改人姓名',
  delete_flag         BOOLEAN      NOT NULL        DEFAULT FALSE
  COMMENT '删除标识',
  PRIMARY KEY (id),
  UNIQUE INDEX u_index_2 (product_category_id),
  INDEX index_0 (create_time),
  INDEX index_1 (creator_id),
  INDEX index_2 (modify_time),
  INDEX index_3 (modify_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '产品种类';

# ALTER TABLE product_category
#   ADD COLUMN handle_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00
# COMMENT '处理费用';