ALTER TABLE product_category
  ADD COLUMN service_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00
COMMENT '处理费用';

ALTER TABLE order_share
  DROP COLUMN partner_id;
ALTER TABLE order_share
  DROP COLUMN partner_fee;

ALTER TABLE order_goods_share
  DROP COLUMN partner_fee;
ALTER TABLE order_goods_share
  DROP COLUMN service_fee;
ALTER TABLE order_goods_share
  DROP COLUMN service_type;
ALTER TABLE order_goods_share
  DROP COLUMN service_id;
ALTER TABLE order_goods_share
  DROP COLUMN partner_id;

ALTER TABLE order_serve
  DROP COLUMN partner_id;
ALTER TABLE order_serve
  DROP COLUMN partner_pre_share;


ALTER TABLE goods
  DROP COLUMN delivery_fee;
ALTER TABLE goods
  DROP COLUMN agency_fee;
ALTER TABLE goods
  DROP COLUMN service_fee;
ALTER TABLE goods
  ADD COLUMN weight DECIMAL(10, 2) NOT NULL DEFAULT 0.00
COMMENT '重量';


ALTER TABLE goods_snapshot
  DROP COLUMN delivery_fee;
ALTER TABLE goods_snapshot
  DROP COLUMN agency_fee;
ALTER TABLE goods_snapshot
  DROP COLUMN service_fee;
ALTER TABLE goods_snapshot
  ADD COLUMN weight DECIMAL(10, 2) NOT NULL DEFAULT 0.00
COMMENT '重量';


ALTER TABLE order_info
  ADD COLUMN service_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00;


ALTER TABLE card_type
  DROP COLUMN agency_fee;
ALTER TABLE card_type
  DROP COLUMN service_fee;

ALTER TABLE order_serve
  DROP COLUMN check_merchant_type;

ALTER TABLE order_share
    ADD COLUMN origin_ad_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00;
ALTER TABLE order_share
    ADD COLUMN origin_service_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00;

ALTER TABLE order_goods_share
    ADD COLUMN origin_ad_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00;

UPDATE order_share SET origin_ad_fee = ad_fee,origin_service_fee = service_fee;

UPDATE order_goods_share SET origin_ad_fee = ad_fee;

UPDATE goods SET weight = 1.00;
UPDATE goods_snapshot SET weight = 1.00;
