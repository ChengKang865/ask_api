# 卡类型表
DROP TABLE IF EXISTS goods_card_type;
CREATE TABLE goods_card_type (
  id           BIGINT(11)  NOT NULL        AUTO_INCREMENT
  COMMENT '物理主键',
  goods_id     VARCHAR(32) NOT NULL
  COMMENT '商品主键，逻辑主键，一个商品只能有一种卡可以兑换',
  card_type_id VARCHAR(32) NOT NULL
  COMMENT '卡片类型表id',
  KEY (id),
  UNIQUE INDEX u_0 (goods_id, card_type_id),
  INDEX index_0 (card_type_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '卡类型表';