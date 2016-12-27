package com.qiekang.api.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiekang.api.utils.Digests;
import com.qiekang.api.utils.ReflectionUtils;

public class WxPayClient extends DefaultPayClient {
	
	private static Logger logger = LoggerFactory.getLogger(WxPayClient.class);
	
	private PrePay prePay;
	
	public WxPayClient(PrePay prePay){
		this.prePay = prePay;
	}
	
	@Override
	public String createSign(Object object, String partnerKey) {
		Map<String, Object> map = ReflectionUtils.bean2Map(object);
		logger.info("post传递之前拼接的map是{}", map);
		
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, Object>> es = map.entrySet();
		Iterator<Entry<String, Object>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
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
	
}
