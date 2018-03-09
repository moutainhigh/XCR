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

//报名信息登记
function enroll_AMeeting(pop_div){
	changeDisabled();
	var province=$("#province option:selected").val();
	var city=$("#city2 option:selected").val();
	var area=$("#area option:selected").val();
	var cityParam=city+area+$("#city3").val();
	if (navigator.onLine){
		if($("#username").val().replace(/\s/g,"")!=""){
			if($("#phone").val().replace(/\s/g,"")!=""){
				if(judgeProv(cityParam)){
					if($("#city3").val().replace(/\s/g,"")!=""){
						if(!(($("#userPhoto")[0].src).indexOf("submit2.png")>0)
								&& !(($("#storePhoto")[0].src).indexOf("submit2.png")>0)){
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
									 "type":1,
									 "username":$("#username").val(),
									 "phone":$("#phone").val(),
									 "userPhoto":$("#userPhoto")[0].src,
									 "storePhoto":$("#storePhoto")[0].src},
									function(result){
										var jsonObj = JSON.parse(result);
										console.log(jsonObj);
										if(jsonObj.Status.State=="M00"){
											if(projectPath.indexOf("/html")>0){
												location.href="/html/annual.html?userId="+$("#userId").val()+"&token="+$("#token").val();
											}else{
												location.href="annual.html?userId="+$("#userId").val()+"&token="+$("#token").val();
											}
										}else if(jsonObj.Status.State=="M07"){//名额已满
											alert(jsonObj.Status.StateDesc);
											if(projectPath.indexOf("/html")>0){
												location.href="/html/annual.html?userId="+$("#userId").val()+"&token="+$("#token").val();
											}else{
												location.href="annual.html?userId="+$("#userId").val()+"&token="+$("#token").val();
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
							alert("本人头像及门头照不能为空");
						}
					}else{
						changeAble();
						alert("详细地址不能为空");
					}
				}else{
					changeAble();
					alert("请选择省份及城市,详细地址不包含省份");
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
function uploadImg(judge,formId){
	console.log("judge:"+judge+"          formId:"+formId);
	var form = new FormData(document.getElementById(formId));
	var src="";
	var flag=true;
	if(judge==1){
		$("#userPhotoPgs").css("display","block");
		$("#userPhotoPgs").val("40");
		flag=ImgSizeAndStyleLevel("userPhotoFile");
		src=projectPath+"/ImgUpload/uploadImg?userId="+$("#userId").val();
		if(projectPath.indexOf("/html")>0){
			src=localhostPaht+"/ImgUpload/uploadImg?userId="+$("#userId").val();
		}
	}else if(judge==2){
		$("#storePhotoPgs").val("40");
		$("#storePhotoPgs").css("display","block");
		flag=ImgSizeAndStyleLevel("storePhotoFile");
		src=projectPath+"/ImgUpload/uploadImg?storeNo="+$("#storeNo").val();
		if(projectPath.indexOf("/html")>0){
			src=localhostPaht+"/ImgUpload/uploadImg?userId="+$("#userId").val();
		}
	}
	console.log("uploadImg验证请求的URL是："+src);
	if(flag){
		$.ajax({
	        url:src,
	        type:"post",
	        data:form,
	        processData:false,
	        contentType:false,
	        success:function(data){
	        	var jsonObj =  JSON.parse(data);
	        	if(jsonObj.code=="200"){
	        		if(judge==1){
	        			$("#userPhotoPgs").val("99");
	            		document.getElementById("userPhoto").src=jsonObj.url;
	            		$("#userPhotoUpdate").html('点击修改<progress  value="0" max="100" class="pgs" id="userPhotoPgs" style="display:none;"></progress>');
	            	}else if(judge==2){
	            		$("#storePhotoPgs").val("99");
	            		document.getElementById("storePhoto").src=jsonObj.url;
	            		$("#storePhotoUpdate").html('点击修改<progress  value="0" max="100" class="pgs" id="storePhotoPgs" style="display:none;"></progress>');
	            	}
	        	}else{
	        		alert("上传图片出错... ...");
	        	}
	        },
	        error:function(data){
	        	console.log("上传失败后的数据："+data);
	        	alert("上传图片出错... ...");
	        }
	    }); 
	}
}
//限制文件大小,限制文件格式
function ImgSizeAndStyleLevel(fileName){
	var filepath=$("input[id='"+fileName+"']").val(); 
	console.log("filepath:"+filepath);
    var extStart=filepath.lastIndexOf("."); 
    var ext=filepath.substring(extStart,filepath.length).toUpperCase();
    if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
    	alert("图片仅限于bmp,png,gif,jpeg,jpg格式");
        return false; 
     } 
    var fileSize = document.getElementById(fileName).files[0].size;
    console.log("fileSize:"+fileSize);
    if(fileSize/1024>fileMaxSize){
    	alert("文件不能超过5M");
    	return false;
    }
    return true;
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

//控制职能输入数字
function onlyNum() {
    if(!(event.keyCode==46)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39))
    if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105)))
    event.returnValue=false;
}

