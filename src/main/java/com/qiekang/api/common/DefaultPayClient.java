package com.qiekang.api.common;

import java.io.IOException;
import java.util.Map;

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

import com.qiekang.api.utils.XMLUtil;
import com.qiekang.api.utils.XmlStream;

public abstract class DefaultPayClient implements PayTemplate {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultPayClient.class);
	
	public abstract String createSign(Object object, String partnerKey);
	
	@Override
	public String postMessage(Object object, String partnerKey, String serverUrl) {
		String sign = createSign(object, partnerKey);
		PrePay prePay = (PrePay)object;
		prePay.setSign(sign);
		
		String readyXml = XmlStream.toXML(prePay);
		logger.info("post过去的xml 为{}", readyXml);
		
		HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(serverUrl);
        try {
            httpPost.setEntity(new StringEntity(readyXml, "UTF-8"));
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

}
