$(function(){
	if(getQueryString("userId")==null && getQueryString("token")==null){
		alert("用户信息异常");
	}else{
		$("#userId").val(getQueryString("userId"));
		$("#userId").val($("#userId").val());
		$("#token").val(getQueryString("token"));
		$("#token").val($("#token").val());
		$("#back").val($.now()*3+1);
		var srcPath=projectPath+"/activity/isEnroll.htm";
		if(projectPath.indexOf("/html")>0){
			srcPath=localhostPaht+"/activity/isEnroll.htm";
		}
		console.log("isEnroll.htm验证请求的URL是："+srcPath);
		$.post(srcPath,
				{"userId":$("#userId").val(),
				"token":$("#token").val(),
				"type":2},
			function(result){
				console.log(result);
				var jsonObj =  JSON.parse(result);
				if(jsonObj.Status.State=="M00"){
					if(jsonObj.enrollResult){
						document.getElementById("oper").src="../xcr/img/svc/cys_05_02.jpg";
						$("#state").val(true);
					}
				}else{
					alert(result);
				}
				$("#storeNo").val(jsonObj.storeNo);
			});
	}
});
function enrollSkip(){
	var srcPath=projectPath+"/activity/skipEnroll.htm";
	if(projectPath.indexOf("/html")>0){
		srcPath=localhostPaht+"/activity/skipEnroll.htm";
	}
	console.log("skipEnroll.htm验证请求的URL是："+srcPath);
	if($("#state").val()=="false"){
		if(navigator.onLine){
			$.post(srcPath,
					{"userId":$("#userId").val(),
					 "token":$("#token").val(),
					 "back":$("#back").val()},
					function(result){
						console.log(result);
						var jsonObj =  JSON.parse(result);
						if(jsonObj.Status.State=="M00"){
							if(projectPath.indexOf("/html")>0){
								location.href="/html/svcEnroll.html?userId="+$("#userId").val()+"&token="+$("#token").val()+"&storeNo="+$("#storeNo").val()+"&back="+$("#back").val();
							}else{
								location.href="svcEnroll.html?userId="+$("#userId").val()+"&token="+$("#token").val()+"&storeNo="+$("#storeNo").val()+"&back="+$("#back").val();
							}
						}else if(jsonObj.Status.State=="M02"){
							alert(jsonObj.Status.StateDesc);
						}else{
							alert(result);
						}
			  		});
		}else{
			alert("你好，你所在地缺网了");
		}
	}
}

