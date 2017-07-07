CREATE TABLE `city_area` (
  `id`         INT(10)      NOT NULL
  COMMENT '城市id编码',
  `area_name`  VARCHAR(50)  NOT NULL
  COMMENT '省/市/区(镇)名称',
  `parent_id`  INT(10)             DEFAULT NULL,
  `short_name` VARCHAR(50)         DEFAULT NULL,
  `area_code`  INT(6)              DEFAULT NULL,
  `zip_code`   INT(10)             DEFAULT NULL,
  `pinyin`     VARCHAR(100)        DEFAULT NULL
  COMMENT '拼音',
  `lng`        VARCHAR(20)         DEFAULT NULL
  COMMENT '经度',
  `lat`        VARCHAR(20)         DEFAULT NULL
  COMMENT '纬度',
  `level`      TINYINT(1)   NOT NULL
  COMMENT '等级',
  `position`   VARCHAR(255) NOT NULL
  COMMENT '地理位置',
  `sort`       TINYINT(3) UNSIGNED DEFAULT '50'
  COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `index_0` (`parent_id`),
  KEY `index_1` (`parent_id`, `area_name`),
  KEY `index_2` (`short_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '城市地区表';
