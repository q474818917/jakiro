package com.qiekang.api.common;

import java.util.Date;
import java.util.Random;

import com.alibaba.fastjson.annotation.JSONField;
import com.qiekang.api.annotation.MapField;
import com.qiekang.api.utils.Digests;

public class WxPay {
	
	@MapField(name = "appId")
	public String appId;
	
	@MapField(name = "nonceStr")
	private String nonceStr;
	
	@JSONField(name = "package")
	@MapField(name = "package")
	private String wxPackage;
	
	@MapField(name = "paySign")
	private String paySign;
	
	@MapField(name = "signType")
	private String signType;
	
	@MapField(name = "timeStamp")
	private String timeStamp;
	
	public WxPay(String appId, String wxPackage, String signType) {
		super();
		this.appId = appId;
		this.wxPackage = wxPackage;
		this.signType = signType;
		this.nonceStr = Digests.MD5(String.valueOf(new Random().nextInt(10000)));
		this.timeStamp = Long.toString(new Date().getTime() / 1000);
	}
	
	public String getWxPackage() {
		return wxPackage;
	}

	public void setWxPackage(String wxPackage) {
		this.wxPackage = wxPackage;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	
}
