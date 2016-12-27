
<html>
<head>

<script type="text/javascript">
	var appId ='${context.get("resultMap")["appId"]}';
	var timestamp ='${context.get("resultMap")["timeStamp"]}';
	var nonceStr ='${context.get("resultMap")["nonceStr"]}';
	var package2 ='${context.get("resultMap")["package"]}';
	var signType ='${context.get("resultMap")["signType"]}';
	var paySign ='${context.get("resultMap")["paySign"]}';
	
	alert(appId + "," + timestamp + "," + nonceStr + "," + package2 + "," + signType + "," + paySign);
	function wxpay(){
		WeixinJSBridge.invoke(
              'getBrandWCPayRequest', {
                  "appId": appId, 
                  "timeStamp": timestamp,         //时间戳，
                  "nonceStr": nonceStr, //随机串
                  "package": package2,
                  "signType": signType,         //微信签名方式：
                  "paySign": paySign //微信签名
              },
              function (res) {
                  if (res.err_msg == "get_brand_wcpay_request:ok") {
                      //支付成功后最好是到后台进行查询一下订单的状态，确保服务器后台相关的业务都已经执行成功。

                  }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                  else if(res.err_msg =='get_brand_wcpay_request:cancel'){
                      
                  }
                  else{
                      //showTip("支付失败。");
                      alert("支付失败" + res.err_code + res.err_desc + res.err_msg);
                  }
              }
      );
	}
</script>

</head>
<body>

	<button onclick="wxpay()" class="btn btn-default">点击支付</button>	

</body>
</html>
