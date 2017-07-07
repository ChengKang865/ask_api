      #购物车商品信息
  DROP TABLE IF EXISTS shopping_cart_goods
  CREATE TABLE shopping_cart_goods (
  		id  BIGINT(11)  NOT NULL        AUTO_INCREMENT
  		COMMENT '物理主键',
  		goods_snapshot_id VARCHAR(32)  NOT NULL
  		COMMENT '商品快照id',
  		shopping_cart_goods_id VARCHAR(32)  NOT NULL
  		COMMENT '逻辑id',
  		user_id VARCHAR(32)  NOT NULL
  		COMMENT '用户id',
  		create_time   TIMESTAMP NULL
  		COMMENT '录入时间',
  		last_update_time   TIMESTAMP NULL
  		COMMENT '最后修改时间',
  		goods_id VARCHAR(32)  NOT NULL
  		COMMENT '商品id',
  		delete_flag   TINYINT(1)  NOT NULL  DEFAULT 0
  		COMMENT '删除位子',
  		delete_time   TIMESTAMP  NOT NULL
  		COMMENT '删除时间',
  		shopping_cart_num   BIGINT  NOT NULL
  		COMMENT '商品数量',
  		goods_id  VARCHAR(32)  NOT NULL
  		COMMENT '商品id',
  )
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '购物车商品信息';