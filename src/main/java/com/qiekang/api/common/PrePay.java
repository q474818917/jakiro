package com.qiekang.api.common;

import static java.util.Arrays.asList;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MemberUsageScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.qiekang.api.annotation.MapField;
import com.qiekang.api.utils.Digests;
import com.qiekang.api.utils.ReflectionUtils;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PrePay {
	
	@XmlElement(name = "appid")
	@MapField(name = "appid")
	private String appId;
	
	@MapField(name = "body")
	private String body;
	
	@XmlElement(name = "mch_id")
	@MapField(name = "mch_id")
	private String mchId;
	
	@XmlElement(name = "nonce_str")
	@MapField(name = "nonce_str")
	private String nonceStr;
	
	@XmlElement(name = "notify_url")
	@MapField(name = "notify_url")
	private String notify_url;
	
	@XmlElement(name = "openid")
	@MapField(name = "openid")
	private String openId;
	
	@XmlElement(name = "out_trade_no")
	@MapField(name = "out_trade_no")
	private String outTradeNo;
	
	private String sign;
	
	@XmlElement(name = "spbill_create_ip")
	@MapField(name = "spbill_create_ip")
	private String spbillCreateIp;
	
	@XmlElement(name = "total_fee")
	@MapField(name = "total_fee")
	private String totalFee;
	
	@XmlElement(name = "trade_type")
	@MapField(name = "trade_type")
	private String tradeType;
	
	public PrePay(){
		
	}
	
	public PrePay(String appId, String mchId, String body, String outTradeNo, String totalFee,
			String spbillCreateIp, String tradeType, String notify_url, String openId) {
		super();
		this.appId = appId;
		this.mchId = mchId;
		this.body = body;
		this.outTradeNo = outTradeNo;
		this.totalFee = totalFee;
		this.spbillCreateIp = spbillCreateIp;
		this.tradeType = tradeType;
		this.notify_url = notify_url;
		this.openId = openId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNonceStr() {
		this.setNonceStr(Digests.MD5(String.valueOf(new Random().nextInt(10000))));
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public static void main(String args[]) {
		String body ="123";
		PrePay prePay = new PrePay(body, body, body, body, body, body, body, body, body);
		Map<String, Object> map = ReflectionUtils.bean2Map(prePay);
		System.out.println(map);
		
		Reflections ref = new Reflections(new ConfigurationBuilder()
                .setUrls(asList(ClasspathHelper.forClass(PrePay.class)))
                .setScanners(
                        new SubTypesScanner(false),
                        new TypeAnnotationsScanner(),
                        new FieldAnnotationsScanner(),
                        new MethodAnnotationsScanner(),
                        new MethodParameterScanner(),
                        new MethodParameterNamesScanner(),
                        new MemberUsageScanner()));
		Set<Field> fieldSet = ref.getFieldsAnnotatedWith(MapField.class);
		Iterator<Field> iteratorField = fieldSet.iterator();
		while(iteratorField.hasNext()){
			System.out.println(iteratorField.next());
		}
	
	}
	
}
