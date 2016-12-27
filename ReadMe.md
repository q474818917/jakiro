###微信支付
 + 扫码支付：使用统一下单接口，trade_type:native
 + 公众号支付：使用统一下单接口，trade_type:JSAPI
 + APP支付
 + 刷卡支付
 + H5支付：内测，用于第三方浏览器支付
 
 先调用https://api.mch.weixin.qq.com/pay/unifiedorder生成预支付订单， 后通过prepay_id进行JSAPI、native、APP支付
 
###支付宝支付
 + 即时到账：构建自动提交表单，service:create_direct_pay_by_user
 + 手机网站支付：构建自动提交表单，service:alipay.wap.create.direct.pay.by.user
 
 https://mapi.alipay.com/gateway.do：旧版，开放平台建立之前
 https://openapi.alipay.com/gateway.do：新版，开放平台建立后



