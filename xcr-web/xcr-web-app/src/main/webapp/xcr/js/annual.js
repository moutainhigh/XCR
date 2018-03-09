//活动开始时间配置
var startDate=new Date("2017/07/01");
//活动结束时间配置
var endDate=new Date("2017/11/21");
//活动最大报名人数
var PERSON_NUM=1000;

$(function(){
	if(getQueryString("userId")==null && getQueryString("token")==null){
		alert("请更新到最新版本并清除缓存后再次尝试");
	}else{
		var currentDate=new Date();
		console.log(currentDate>=startDate);
		console.log(currentDate<=endDate);
		if(currentDate>startDate && currentDate<endDate){
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
					"type":1},
				function(result){
					console.log(result);
					var jsonObj =  JSON.parse(result);
					if(jsonObj.Status.State=="M00"){
						if(jsonObj.isActivityDate==1){
							if(jsonObj.enrollResult){
								document.getElementById("annual-button").src="../xcr/img/btn_01.png";
								document.getElementById("annual-success").style.display="block";
								$("#state").val(true);
							}
						}else if(jsonObj.isActivityDate==0){//活动还没有开始
							$("#state").val(true);
						}else if(jsonObj.isActivityDate==2){//活动已经截止
							$("#state").val(true);
						}
					}else if(jsonObj.Status.State=="M00"){
						alert(jsonObj.Status.StateDesc);
					}else{
						alert(result);
					}
					$("#storeNo").val(jsonObj.storeNo);
				});
		}else if(currentDate<startDate){
			console.log("当前日期小于活动开始日期");
			$("#state").val(true);
		}else if(currentDate>endDate){
			console.log("当前日期大于活动结束日期");
			$("#state").val(true);
		}
	}
});
function enrollSkip(){
	var srcPath=projectPath+"/activity/skipEnroll.htm";
	if(projectPath.indexOf("/html")>0){
		srcPath=localhostPaht+"/activity/skipEnroll.htm";
	}
	console.log("skipEnroll.htm验证请求的URL是："+srcPath);
	if (navigator.onLine){
		if($("#state").val()=="false"){
			$.post(srcPath,
				{"userId":$("#userId").val(),
				 "token":$("#token").val(),
				 "back":$("#back").val()},
				function(result){
					console.log(result);
					var jsonObj =  JSON.parse(result);
					if(jsonObj.Status.State=="M00"){
						if(projectPath.indexOf("/html")>0){
							location.href="/html/annualEnroll.html?userId="+$("#userId").val()+"&token="+$("#token").val()+"&storeNo="+$("#storeNo").val()+"&back="+$("#back").val();
						}else{
							location.href="annualEnroll.html?userId="+$("#userId").val()+"&token="+$("#token").val()+"&storeNo="+$("#storeNo").val()+"&back="+$("#back").val();
						}
					}else if(jsonObj.Status.State=="M02"){
						alert(jsonObj.Status.StateDesc);
					}else{
						alert(result);
					}
		  	});
		}
	}else{
		alert("你好，你所在地缺网了");
	}
}

