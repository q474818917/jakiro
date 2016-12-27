package com.qiekang.verticle;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.qiekang.api.common.PayTemplate;
import com.qiekang.api.common.PrePay;
import com.qiekang.api.common.WxPay;
import com.qiekang.api.common.WxPayClient;
import com.qiekang.api.utils.Digests;
import com.qiekang.api.utils.ReflectionUtils;
import com.qiekang.api.utils.XMLUtil;
import com.qiekang.utils.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

public class WxpayVerticle extends AbstractVerticle {

	private static Logger logger = LoggerFactory.getLogger(IndexVerticle.class);

	final FreeMarkerTemplateEngine engine = FreeMarkerTemplateEngine.create();

	String serverUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	String appId = "";
	String partnerKey = "";
	String mchId = "";
	
	String notifyUrl = "http://127.0.0.1/notify";

	public static void main(String[] args) {
		Runner.runExample(WxpayVerticle.class);
	}

	@Override
	public void start() throws Exception {
		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());

		final FreeMarkerTemplateEngine engine = FreeMarkerTemplateEngine.create();

		router.get("/payment/index").handler(ctx -> {
			
			PrePay prePay = new PrePay(appId, mchId, "商品名称", "201612039234", 20 * 100 + "", "127.0.0.1", "JSAPI", notifyUrl, "ow6YaxLxgshmDCRX34WREf-ECoL0");
			PayTemplate payTemplate = new WxPayClient(prePay);
			String prepayId = payTemplate.postMessage(prePay, partnerKey, serverUrl);
			
			WxPay wxPay = new WxPay(appId, "prepay_id=" + prepayId, "MD5");
			wxPay.setPaySign(new WxPayClient(prePay).createSign(wxPay, partnerKey));
			System.out.println(JSON.toJSONString(wxPay));/*
			
			SortedMap<String, String> sortMap = this.buildPrePayParam("商品名称", 20, "201612039234", "JSAPI", "ow6YaxLxgshmDCRX34WREf-ECoL0");
			String prepayId = this.getPerPayId(sortMap);
			Map<String, String> resultMap = this.createPackageValue(prepayId);*/
			ctx.put("resultMap", ReflectionUtils.bean2Map(wxPay));
			engine.render(ctx, "templates/wxpay.ftl", res -> {
				if (res.succeeded()) {
					ctx.response().putHeader("contentType", "text/html;charset=UTF-8");
					ctx.response().end(res.result());
				} else {
					ctx.fail(res.cause());
				}
			});
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}
	
    private SortedMap<String, String> buildPrePayParam(String body, double price, String tradeNo, String type, String openid) {
        SortedMap<String, String> info = Maps.newTreeMap();
        info.put("appid", appId);
        info.put("mch_id", mchId);
        info.put("nonce_str", genNonceStr());
        info.put("body", body);
        info.put("out_trade_no", tradeNo);
        price = (price * 100);
        info.put("total_fee", (int) Math.abs(price) + "");
        info.put("spbill_create_ip", "196.168.3.25");
        info.put("trade_type", type);
        info.put("notify_url", notifyUrl);

        if (openid != null && !openid.equals("")) {
            info.put("openid", openid);
        }
        return info;
    }
    
    /**
     * 生成随机数
     *
     * @return
     */
    private String genNonceStr() {
        Random random = new Random();
        return Digests.MD5(String.valueOf(random.nextInt(10000)));
    }
    
    private String getPerPayId(SortedMap<String, String> map) {
        String xml = getPackageSign(map);
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(serverUrl);
        try {
            httpPost.setEntity(new StringEntity(xml, "UTF-8"));
            HttpResponse resp = client.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            return getXMLValue(entity, "prepay_id");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String getPackageSign(SortedMap<String, String> map) {

        map.put("sign", createSign(map));
        return buildXML(map);
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    private String createSign(SortedMap<String, String> packageParams) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + partnerKey);
        String sign = Digests.MD5(sb.toString()).toUpperCase();
        return sign;
    }

    private String buildXML(SortedMap<String, String> map) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>\n");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if ("body".equals(entry.getKey()) || "sign".equals(entry.getKey())) {
                xml.append("<" + entry.getKey() + "><![CDATA[").append(entry.getValue()).append("]]></" + entry.getKey() + ">\n");
            } else {
                xml.append("<" + entry.getKey() + ">").append(entry.getValue()).append("</" + entry.getKey() + ">\n");
            }
        }

        xml.append("</xml>");
        return xml.toString();
    }
    
    /**
     * 根据key获取xml值
     *
     * @param result
     * @param key
     * @return
     */
    private static String getXMLValue(HttpEntity result, String key) {
        //获取查询结果
        try {
            String resource = EntityUtils.toString(result, "UTF-8");
            resource = resource.replace("<![CDATA[", "").replace("]]>", "");
            Map<String, String> param = XMLUtil.doXMLParse(resource);
//            System.out.println(resource);
            return param.get(key);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> createPackageValue(String prepayid) {
        SortedMap<String, String> map = Maps.newTreeMap();
        //设置package订单参数
        map.put("appId", appId);
//        map.put("device_info", deviceInfo);
        map.put("timeStamp", getTimestamp());
        map.put("nonceStr", genNonceStr());
        //JSAPI 传参形式
        map.put("package", "prepay_id=" + prepayid);
        //APP 传参形式
//        map.put("package", "Sign=WXPay");
        map.put("signType", "MD5");
        map.put("paySign", createSign(map));

        return map;
    }
    
    private String getTimestamp() {
        return Long.toString(new Date().getTime() / 1000);
    }





}
