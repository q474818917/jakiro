package com.qiekang.verticle;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiekang.api.common.AlipaySubmit;
import com.qiekang.common.config.AlipayConfig;
import com.qiekang.utils.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

public class AlipayVerticle extends AbstractVerticle {
	
	private static Logger logger = LoggerFactory.getLogger(IndexVerticle.class);
	
	final FreeMarkerTemplateEngine engine = FreeMarkerTemplateEngine.create();
	
	String serverUrl = "https://openapi.alipay.com/gateway.do";
	
	String appId  = "2088611557852048";
	
	String privateKey = "";
	
	public static void main(String[] args) {
	    Runner.runExample(AlipayVerticle.class);
	  }
	
	@Override
	public void start() throws Exception {
		Router router = Router.router(vertx);
		
	    router.route().handler(BodyHandler.create());

	    final FreeMarkerTemplateEngine engine = FreeMarkerTemplateEngine.create();
	    

	    router.get("/index").handler(ctx -> {
	    	Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", AlipayConfig.service);
	        sParaTemp.put("partner", AlipayConfig.partner);
	        sParaTemp.put("seller_id", AlipayConfig.seller_id);
	        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
			sParaTemp.put("payment_type", AlipayConfig.payment_type);
			sParaTemp.put("notify_url", AlipayConfig.notify_url);
			sParaTemp.put("return_url", AlipayConfig.return_url);
			sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
			sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
			sParaTemp.put("out_trade_no", "2000102332");
			sParaTemp.put("subject", "苹果MP4");
			sParaTemp.put("total_fee", "3800");
			sParaTemp.put("body", "苹果");
			//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
	        //如sParaTemp.put("参数名","参数值");
			
			//建立请求
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
	      ctx.put("name", sHtmlText);

	      engine.render(ctx, "templates/alipay.ftl", res -> {
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
	
}
