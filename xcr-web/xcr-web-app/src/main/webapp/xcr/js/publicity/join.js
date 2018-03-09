/**
 * 
 */
$("#fPhone").blur(function(){
	console.log("===");
});

$("#button1").click(function(){
//	{"fName":fName,
//		 "fPhone":fPhone,
//		 "storeOpen":$("input[name='storeOpen']:checked").val(),
//		 "detailAddress":detailAddress}
	disabledTrue();
	if (navigator.onLine){
		var fName=$("#fName").val();
		var fPhone=$("#fPhone").val();
		var detailAddress=$("#detailAddress").val();
		var flag=checkparam(fName,fPhone,detailAddress);
		if(flag){
			var srcPath=projectPath+"/User/joinEnroll";
			if(projectPath.indexOf("/html")>0){
				srcPath=localhostPaht+"/User/joinEnroll";
			}
			$.ajax({
		        url:srcPath,
		        type:"post",
		        data:new FormData(document.getElementById("form")),
		        processData:false,
		        contentType:false,
		        success:function(data){
		        	var jsonObj =  JSON.parse(data);
		        	console.log(jsonObj);
					if(jsonObj.result){
						alert(jsonObj.msg);
					}else{
						alert(jsonObj.msg);
						disabledFalse();
					}
		        },
		        error:function(data){
		        	 alert("系统繁忙，请稍后重试..."); 
					  disabledFalse();
		        }
		    }); 
		}else{
			disabledFalse();
		}
	}else{
		alert("你好，你所在地缺网了");
		disabledFalse();
	}
});

function disabledTrue(){
	$("#button1").css("background","rgb(169, 169, 169)");
	$("#button1").attr("disabled",true);
}
function disabledFalse(){
	$("#button1").css("background","#E51C26");
	$("#button1").attr("disabled",false);
}

function checkparam(fName,fPhone,detailAddress){
	if(fName.length==0){
		alert("姓名不能为空");
		return false;
	}
	if(fPhone.length==0){
		alert("电话不能为空");
		return false;
	}
	if(fPhone.length!=11){
		alert("电话格式不正确");
		return false;
	}
	if(detailAddress.length==0){
		alert("门店地址不能为空");
		return false;
	}
	return true;
}