## alipay_wap 支付宝手机网页支付

> 说明: extra字段中，必须要传 success_url 参数.如果

 * success_url
    必须!!! string
    支付成功的回调地址,一般是支付成功的提示页面。
 * cancel_url
    可选, string
    支付取消的回调地址， app_pay 为true时，该字段无效。
 * new_version
    可选, boolean
    2016 年 6 月 16 日之前登录 Ping++ 管理平台填写支付宝手机网站的渠道参数的旧接口商户，需要更新接口时设置此参数值为true，6月16号后接入的新接口商户不需要设置该参数。
 * app_pay
    可选, boolean
    是否使用支付宝客户端支付，该参数为true时，调用客户端支付。
 * buyer_account
    response-only, string
    支付完成将额外返回付款用户的支付宝账号。
    
Demo:

```
HashMap<String, String> extraMap = new HashMap<>();
extraMap.put("success_url", "http://www.autoask.com/");
Charge charge = PingppChargeUtil.createCharge(1, "AutoASK 测试", "autoask测试", "58.240.32.162", "00000001234",
        "alipay_wap", extraMap);
```

接口返回结果:

```
返回结果:

	{
	  "id": "ch_5KGSO8GSW5GSTOGS8Kbnnrf1",
	  "object": "charge",
	  "created": 1478019361,
	  "livemode": true,
	  "paid": false,
	  "refunded": false,
	  "app": "app_zXTSKC4GyLqTSCKC",
	  "channel": "alipay_wap",
	  "order_no": "00000001234",
	  "client_ip": "58.240.32.162",
	  "amount": 1,
	  "amount_settle": 1,
	  "currency": "cny",
	  "subject": "AutoASK 测试",
	  "body": "autoask测试",
	  "time_paid": null,
	  "time_expire": 1478105761,
	  "time_settle": null,
	  "transaction_no": null,
	  "refunds": {
		"object": "list",
		"url": "/v1/charges/ch_5KGSO8GSW5GSTOGS8Kbnnrf1/refunds",
		"has_more": false,
		"data": []
	  },
	  "amount_refunded": 0,
	  "failure_code": null,
	  "failure_msg": null,
	  "metadata": {},
	  "credential": {
		"object": "credential",
		"alipay_wap": {
		  "channel_url": "https://mapi.alipay.com/gateway.do",
		  "service": "alipay.wap.create.direct.pay.by.user",
		  "_input_charset": "utf-8",
		  "notify_url": "https://notify.pingxx.com/notify/charges/ch_5KGSO8GSW5GSTOGS8Kbnnrf1",
		  "partner": "2088421426870468",
		  "out_trade_no": "00000001234",
		  "subject": "AutoASK 测试",
		  "body": "autoask测试",
		  "total_fee": "0.01",
		  "payment_type": 1.0,
		  "seller_id": "2088421426870468",
		  "it_b_pay": "2016-11-03 00:56:01",
		  "return_url": "http://www.autoask.com/",
		  "sign": "os9IaI1R0ZihRq0dZvzDFZbKRCkfinHU+S9lJunOhu5wMR9IJzpi0u3HOQCLWCXAGjsObpsrD1CHK3TSAc4DtdYisMOL4n10EVyxzWMuRRYe/qxZHLy+gP5hnWBntNb0SJDYClsmbHymQglCIJaIpGFbK7I8onKTiH98dSaeBUw=",
		  "sign_type": "RSA"
		}
	  },
	  "extra": {
		"success_url": "http://www.autoask.com/"
	  },
	  "description": null
	}


```

## alipay_pc_direct	支付宝 PC 网页支付

> 说明: extra字段中，必须要传 success_url 参数

  * success_url
    必须!!! string
    支付成功的回调地址,一般是支付成功的提示页面。
  * enable_anti_phishing_key
    可选, boolean
    是否开启防钓鱼网站的验证参数（如果已申请开通防钓鱼时间戳验证，则此字段必填）
  * exter_invoke_ip
    可选, string
    客户端 IP ，用户在创建交易时，该用户当前所使用机器的IP（如果商户申请后台开通防钓鱼IP地址检查选项，此字段必填，校验用）。
Demo:

```
HashMap<String, String> extraMap = new HashMap<>();
extraMap.put("success_url", "http://www.autoask.com/");
Charge charge = createCharge(1, "AutoASK 测试", "autoask测试", "58.240.32.162", "00000001234",
        "alipay_pc_direct", extraMap);
System.out.println(charge);
```

接口返回结果:

```

{
  "id": "ch_4evXPGGy1e949O8u5KWn58OS",
  "object": "charge",
  "created": 1478020220,
  "livemode": true,
  "paid": false,
  "refunded": false,
  "app": "app_zXTSKC4GyLqTSCKC",
  "channel": "alipay_pc_direct",
  "order_no": "00000001234",
  "client_ip": "58.240.32.162",
  "amount": 1,
  "amount_settle": 1,
  "currency": "cny",
  "subject": "AutoASK 测试",
  "body": "autoask测试",
  "time_paid": null,
  "time_expire": 1478106620,
  "time_settle": null,
  "transaction_no": null,
  "refunds": {
    "object": "list",
    "url": "/v1/charges/ch_4evXPGGy1e949O8u5KWn58OS/refunds",
    "has_more": false,
    "data": []
  },
  "amount_refunded": 0,
  "failure_code": null,
  "failure_msg": null,
  "metadata": {},
  "credential": {
    "object": "credential",
    "alipay_pc_direct": {
      "service": "create_direct_pay_by_user",
      "_input_charset": "utf-8",
      "return_url": "http://www.autoask.com/",
      "notify_url": "https://notify.pingxx.com/notify/charges/ch_4evXPGGy1e949O8u5KWn58OS",
      "partner": "2088421426870468",
      "out_trade_no": "00000001234",
      "subject": "AutoASK 测试",
      "body": "autoask测试",
      "total_fee": "0.01",
      "payment_type": 1.0,
      "seller_id": "2088421426870468",
      "it_b_pay": "1d",
      "sign": "Z731dBZOL4PtbpUeMK+O6BuxRwe/Vn4sfEtwTHfqdnKM/kzRT4BTuFTNZpXYaLv8XaE6mh3FaCuVLRVXK0uG/lWnhugjss3YAVMUjEhThjg2iG0uMYY+tgznGSVWpzyqM7R943ShEfQEpJA7x7qkf/hOcY3q6AD9dPIA50IHkto=",
      "sign_type": "RSA"
    }
  },
  "extra": {
    "success_url": "http://www.autoask.com/"
  },
  "description": null
}
```