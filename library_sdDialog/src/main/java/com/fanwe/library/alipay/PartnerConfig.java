package com.fanwe.library.alipay;

public class PartnerConfig
{
	// 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
	public static final String PARTNER = "";
	// 账户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
	public static final String SELLER = "";
	// 私钥: genrsa -out d:\openssl\prv.pem 1024
	// 公钥: rsa -in d:\openssl\prv.pem -pubout -out d:\openssl\pub.pem 要上传到
	// ms.alipay.com 上
	// openssl pkcs8 -topk8 -inform PEM -in d:\openssl\prv.pem -outform PEM
	// -nocrypt
	// 商户（RSA）私钥
	public static final String RSA_PRIVATE = "";
	// 支付宝（RSA）公钥 用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
	public static final String RSA_ALIPAY_PUBLIC = "";

	public static final String ALIPAY_PLUGIN_NAME = "alipay_plugin510.apk";

	public final static String server_url = "https://msp.alipay.com/x.htm";
}
