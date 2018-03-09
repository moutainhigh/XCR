//用于校验有没有上传图片
var checkImgCode="";
//文件上传最大大小（单位：KB）
var fileMaxSize="5120";
//判断是不是数字
$(function(){
	isNum();
})
//校验是不是数字
function isNum(){
	$('input #phone').keyup(function(){  
        var c=$(this);  
        if(/[^\d]/.test(c.val())){//替换非数字字符  
          var temp_amount=c.val().replace(/[^\d]/g,'');  
          $(this).val(temp_amount);  
        }  
     });
}
function validate(){
	var obj=$("#phone").val();
    var reg = /^[0-9]*$/;
    if(!reg.test(obj)){
    	$("#phone").val("");
    }
};

//报名信息登记
function enroll_AMeeting(pop_div){
	changeDisabled();
	var province=$("#province option:selected").val();
	var city=$("#city2 option:selected").val();
	var area=$("#area option:selected").val();
	console.log(area);
	var cityParam=city+area+$("#city3").val();
	if (navigator.onLine){
		if($("#username").val().replace(/\s/g,"")!=""){
			if($("#phone").val().replace(/\s/g,"")!=""){
				if(judgeProv(cityParam)){
					if($("#city3").val().replace(/\s/g,"")!=""){
						var srcPath=projectPath+"/activity/enrollInfo.htm";
						if(projectPath.indexOf("/html")>0){
							srcPath=localhostPaht+"/activity/enrollInfo.htm";
						}
						console.log("enrollInfo.htm验证请求的URL是："+srcPath);
						$.post(srcPath,
								{"userId":$("#userId").val(),
								 "token":$("#token").val(),
								 "back":$("#back").val(),
								 "storeNo":$("#storeNo").val(),
								 "province":province,
								 "city":cityParam,
								 "type":2,
								 "username":$("#username").val(),
								 "phone":$("#phone").val()},
								function(result){
									var jsonObj = JSON.parse(result);
									console.log(jsonObj);
									if(jsonObj.Status.State=="M00"){
										if(projectPath.indexOf("/html")>0){
											location.href="/html/svcIndex_annual.html?userId="+$("#userId").val()+"&token="+$("#token").val();
										}else{
											location.href="svcIndex_annual.html?userId="+$("#userId").val()+"&token="+$("#token").val();
										}
									}else if(jsonObj.Status.State=="M07"){//名额已满
										alert(jsonObj.Status.StateDesc);
										if(projectPath.indexOf("/html")>0){
											location.href="/html/svcIndex_annual.html?userId="+$("#userId").val()+"&token="+$("#token").val();
										}else{
											location.href="svcIndex_annual.html?userId="+$("#userId").val()+"&token="+$("#token").val();
										}
									}else if(jsonObj.Status.State=="M02"){
										alert(jsonObj.Status.StateDesc);
										changeAble();
									}else{
										alert(result);
									}
						  });
					}else{
						changeAble();
						alert("详细地址不能为空");
					}
				}else{
					changeAble();
					alert("请选择省份及城市,详细地址不包含省份、城市、区");
				}
			}else{
				changeAble();
				alert("手机号不能为空");
			}
		}else{
			changeAble();
			alert("用户名不能为空");
		}
	}else{
		alert("你好，你所在地缺网了");
		changeAble();
	}
}
//提交按钮置灰不可用
function changeDisabled(){
	$("#submitEnroll").attr("disabled",true);
	$("#submitEnroll").css("background-color","#dedee2");
}
//提交按钮恢复可用
function changeAble(){
	$("#submitEnroll").attr("disabled",false);
	$("#submitEnroll").css("background-color","#da251d");
}

