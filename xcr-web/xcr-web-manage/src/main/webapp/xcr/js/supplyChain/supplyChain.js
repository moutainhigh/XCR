/**
 * 供应链商品首页推荐新增或修改
 */
var px1="575*575";
var px2="486*278";
var px4="348*200";
var px7="1080*317";

$(function(){
	var method=$("#method").val();
	var tbodyHtml='<tr><input type="hidden" class="index" value="0"><input type="hidden" class="adsId" value=""><input type="hidden" class="skipURL" value=""><td style="text-align: center; width: 10%;" class="position"></td>';
	 	tbodyHtml+='<td style="text-align: center; width: 20%;" class="modifyTime"></td>';
	 	tbodyHtml+='<td style="text-align: center; width: 30%;" class="skipURL2"></td>';
	 	tbodyHtml+='<td style="text-align: center; width: 10%;">';
	 	tbodyHtml+='<p class="triggerPicShowEnlarge"  rel="" data-id="" onmouseover="showEnlargeImg(this);">';
	 	tbodyHtml+='<img  class="imgURL" src=""></p></td>';
	 	tbodyHtml+='<td style="text-align: center; width: 10%;" class="px"></td>';
	 	tbodyHtml+='<td style="text-align: center; vertical-align: middle; width: 20%; ">';
	 	tbodyHtml+='<div class="action" style="cursor:pointer;">';
	 	tbodyHtml+='</div></td></tr>';
	switch(parseInt(method)){
	case 0:
		console.log("供应链推荐商品管理----->新增页面");
		for (var i = 0; i < 7; i++) {
	 		$("#tbody").append(tbodyHtml);
	 		$(".position").eq(i).html((i+1));
	 		$(".action").eq(i).append('<a onclick="edit('+(i+1)+')"><h4><i class="fa fa-pencil-square-o"></i> 修改</h4></a>');
	 		$(".index").eq(i).val((i+1));
	 		switch(i){
	 		case 0:
	 			$(".px").eq(i).text(px1);
	 			break;
	 		case 1:case 2:
	 			$(".px").eq(i).text(px2);
	 			break;
	 		case 3:case 4:case 5:
	 			$(".px").eq(i).text(px4);
	 			break;
	 		case 6:
	 			$(".px").eq(i).text(px7);
	 			break;
	 		}
	 		
		}
		break;
	case 1:
		console.log("供应链推荐商品管理----->修改页面");
		break;
	default:
		win.alert("温馨提示","请求出错了，请重试",null);
		break;
	}
});

function showEnlargeImg(obj) {
    $(".triggerPicShowEnlarge").each(function () {
        $(this).powerFloat({
            targetMode: "ajax"
        })
    });
    $(obj).powerFloat({
        targetMode: "ajax"
    });
}

function edit(index){
	$("#index").val(index);
	$("#myModalTitle").html('<label>修改</label> <span id="positionSet" style="margin-left:10px;"></span>');
	$("#positionSet").text("位置"+index);
	var html='<table>';
		html+='<tr><td style="text-align:right;width: 20%;height: 60px;padding-right:20px;"><span style="color:#f00;">*</span>跳转链接</td><td><input type="text" class="form-control" style="border-radius:5px;width:96%;" id="url"/></td></tr>';
		html+='<tr><td style="text-align:right;padding-right:20px;"><span style="color:#f00;">*</span>图片</td><td>（图片大小不超过1M，建议像素大小<label id="pxSuggest" style=""></label>）</td></tr>';
		html+='<tr><td style="text-align:right;padding-right:20px;"></td><td><div style="margin:0 auto;"><img id="showImage" src="" style="';
		switch(index){
		case 1:
			html+='width:150px;height:150px;';
			break;
		case 2:case 3:
			html+='width:262px;height:150px;';
			break;
		case 4:case 5:case 6:
			html+='width:240px;height:150px;';
			break;
		case 7:
			html+='width:320px;height:71px;';
			break;
		}
		html+='border: 3px dotted #d2d6de;border-radius: 4px;"/>';
		html+='<form id="fileForm" action="/xcr/fileupload/single.htm" method="post" enctype="multipart/form-data">';
		html+='<a onclick="" class="file">点击修改<input id="imgTest" name="file" type="file" value="" onchange="imgChange(event)" accept=".jpg,.png"></a><';
		html+='/form></div></td></tr></table>';
	$("#myModalContent").html(html);
	switch(index){
	case 1:
		$("#pxSuggest").text("575*575");
		break;
	case 2:case 3:
		$("#pxSuggest").text("486*278");
		break;
	case 4:case 5:case 6:
		$("#pxSuggest").text("348*200");
		break;
	case 7:
		$("#pxSuggest").text("1080*317");
		break;
	}
	var tempIndex=(index-1);
	console.log("index:"+index+"     url:"+$(".skipURL").eq(tempIndex).val()+"    imgURL:"+$(".imgURL").eq(tempIndex)[0].src);
	$("#url").val($(".skipURL").eq(tempIndex).val());
	var url=$(".imgURL").eq(tempIndex)[0].src;
	if(!(url.indexOf("main.htm")>=0)){
		$("#showImage").attr("src",$(".imgURL").eq(tempIndex)[0].src);
	}
	$("#model").modal("toggle");
}
//用于前端展示图片
function imgChange(e) {
//  console.info(e.target.files[0]);//图片文件
  var dom =$("input[id^='imgTest']")[0];
  console.info(dom.value);//这个是文件的路径 C:\fakepath\icon (5).png
  console.log(e.target.value);
  var reader = new FileReader();
  reader.onload = (function (file) {
      return function (e) {
//        console.info(this.result); //这个就是base64的数据了
          var sss=$("#showImage");
          $("#showImage")[0].src=this.result;
      };
  })(e.target.files[0]);
  console.log("图片的占用空间大小："+e.target.files[0].size);
  if(e.target.files[0].size>1024*1024){
	  win.alert("温馨提示","上传图片大小超过1M",null);
  }else{
	  reader.readAsDataURL(e.target.files[0]);
	  uploadImg();
  }
}
/**
 * 用于上传图片具体实现
 */
function uploadImg(){
	console.log("现在开始执行上传图片的方法");
	var actionUrl="/xcr/fileupload/single.htm";
	$("#fileForm").attr("action",actionUrl);
	var form = new FormData(document.getElementById("fileForm"));
	$.ajax({
        url:actionUrl,
        type:"post",
        data:form,
        processData:false,
        contentType:false,
        success:function(data){
        	console.log("上传图片返回的数据："+data);
        	if(data.indexOf("http")){
        		$("#showImage").attr("src",data.substring(16));
        	}else{
        		win.alert("温馨提示","上传图片失败，请重新上传",null);
        	}
        },
        error:function(data){
        	console.log("上传图片失败，返回数据："+data);
        	win.alert("温馨提示","上传图片失败，请重新上传",null);
        }
    }); 
}
/**
 * 编辑好图片之后点击确定
 */
function sure(){
	console.log("确定修改完成。。。。。。");
	console.log("图片的链接是。。。。。。"+$("#showImage")[0].src);
	var index=$("#index").val();
	if($("#url").val()!="" && ($("#showImage")[0].src.indexOf("main.htm")<0)){
		$(".index").eq(index-1).val(1);
		var url=$("#url").val();
		if(parseInt($("#url").val().length)>75){
			url=$("#url").val().substring(0,75);
		}
		$(".skipURL2").eq(index-1).html(url);
		$(".skipURL").eq(index-1).val($("#url").val());
		$(".triggerPicShowEnlarge").eq(index-1).attr("rel", $("#showImage")[0].src);
		$(".imgURL").eq(index-1).attr("src", $("#showImage")[0].src);
		var img=new Image();
		img.src = $("#showImage")[0].src;
//		$(".px").eq(index-1).text(img.width+"*"+img.height);
		$("#showImage")[0].src;
		$("#model").modal("hide");
	}else{
		win.alert("温馨提示","跳转链接与图片不能为空哟",null);
	}
}
/**
 * 针对保存条件判断
 */
function save(){
	var method=$("#method").val();
	var groupName=$("#groupName").val();
	if(groupName==""){
		win.alert("温馨提示","推荐主题不能为空哟",null);
	}else{
		if(parseInt(method)==0){
			var count=0;
			for (var i = 0; i < $(".index").length; i++) {
				if(parseInt($(".index").eq(i).val())==1){
					count+=1;
				}
			}
			if(count==7){
				saveImpl();
			}else{
				win.alert("温馨提示","新增推荐商品必须满7个才能保存哟",null);
			}
			count=0;
		}else if(parseInt(method)==1){
			saveImpl();
		}
	}
}
/**
 * 执行实际的保存操作
 */
function saveImpl(){
	$.ajax({
		type: "POST",
		url: "/xcr/sc/scAddOrUpdate.htm",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(pachData()),
		dataType: "json",
		success: function (data) {
			console.log(data);
			if(data.success){
				loadHtml('xcr/sc/scGroup.htm');
			}else{
				console.log("添加失败1===="+data);
				$('#btn_add1').removeAttr("disabled"); 
				win.alert("错误提示",data.errorMessage,null);
			}
		},
		error: function(data) {
			console.log("添加失败2===="+data);
			$('#btn_add1').removeAttr("disabled"); 
			win.alert("错误提示","服务器正忙，请稍后重试",null);
		}
	});
}


function pachData(){
	var method=$("#method").val();
	var groupId=$("#groupId").val();
	var groupName=$.trim($("#groupName").val());
	var adsDtoArr=[];
	for (var i = 0; i < $(".adsId").length; i++) {
		console.log("index的值："+parseInt($(".index").eq(i).val()));
		if(parseInt($(".index").eq(i).val())==1){
			var adsDto="";
			if(parseInt(method)==0){
				adsDto=new adsDtoObjAdd($(".position").eq(i).text(),$(".imgURL").eq(i)[0].src,$(".skipURL").eq(i).val());
			}else if(parseInt(method)==1){
				adsDto=new adsDtoObjUpdate($(".adsId").eq(i).val(),$(".position").eq(i).text(),$(".imgURL").eq(i)[0].src,$(".skipURL").eq(i).val());
			}
			adsDtoArr[adsDtoArr.length]=adsDto;
		}
	}
	var json = {};
	if(parseInt(method)==0){
		json = {
				"back": groupName,
				"paramList": adsDtoArr
		};
	}else if(parseInt(method)==1){
		json = {
				"id":groupId,
				"back": groupName,
				"paramList": adsDtoArr
		};
	}
	console.log("请求的添加或者更新的数据为："+JSON.stringify(json));
	return json;
}
function adsDtoObjUpdate(id,sore,picUrl,activityUrl){
	this.id=id;
	this.sore=sore;
	this.picUrl=picUrl;
	this.activityUrl=activityUrl;
}
function adsDtoObjAdd(sore,picUrl,activityUrl){
	this.sore=sore;
	this.picUrl=picUrl;
	this.activityUrl=activityUrl;
}