DROP TABLE IF EXISTS `car_category`;

CREATE TABLE `car_category` (
  `id`                 INT(11)      NOT NULL AUTO_INCREMENT,
  `source_id`          INT(11)               DEFAULT NULL,
  `name`               VARCHAR(50)           DEFAULT NULL,
  `alias`              VARCHAR(50)           DEFAULT NULL,
  `slug`               VARCHAR(32)           DEFAULT NULL,
  `url`                VARCHAR(200) NOT NULL DEFAULT '',
  `parent`             VARCHAR(32)           DEFAULT NULL,
  `checker_runtime_id` INT(11)               DEFAULT NULL,
  `keywords`           VARCHAR(100)          DEFAULT NULL,
  `classified`         VARCHAR(32)           DEFAULT NULL,
  `classified_url`     VARCHAR(200)          DEFAULT NULL,
  `slug_global`        VARCHAR(32)           DEFAULT NULL,
  `logo_img`           VARCHAR(200)          DEFAULT NULL,
  `mum`                VARCHAR(32)           DEFAULT NULL,
  `first_letter`       VARCHAR(1)            DEFAULT NULL,
  `has_detailmodel`    INT(11)      NOT NULL,
  `starting_price`     DECIMAL(10, 1)        DEFAULT NULL,
  `classified_slug`    VARCHAR(128)          DEFAULT NULL,
  `thumbnail`          VARCHAR(200)          DEFAULT NULL,
  `pinyin`             VARCHAR(32)           DEFAULT NULL,
  `status`             VARCHAR(1)            DEFAULT NULL,
  `attribute`          VARCHAR(10)           DEFAULT NULL,
  `units`              INT(11)      NOT NULL,
  `popular`            VARCHAR(1)            DEFAULT NULL,
  `on_sale`            TINYINT(1)            DEFAULT '0',
  `score`              INT(11)      NOT NULL DEFAULT '0',
  `normalized_name`    VARCHAR(255)          DEFAULT NULL,
  `brand_area`         VARCHAR(20)           DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `open_category_slug` (`slug`),
  KEY `open_category_89f89e85` (`source_id`),
  KEY `open_category_e2a2b9dd` (`checker_runtime_id`),
  KEY `open_category_4d9cd3e5` (`keywords`),
  KEY `open_category_336a332` (`pinyin`),
  KEY `open_category_cbfce4f2` (`attribute`),
  KEY `open_category_3761f8b5` (`parent`),
  KEY `name` (`name`),
  CONSTRAINT `parent_refs_slug_68a82c0955ff4399` FOREIGN KEY (`parent`) REFERENCES `car_category` (`slug`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2707
  DEFAULT CHARSET = utf8
  COMMENT '品牌-型号库';