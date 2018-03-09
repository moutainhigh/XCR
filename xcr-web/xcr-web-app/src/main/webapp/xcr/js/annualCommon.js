//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp  
var curWwwPath=window.document.location.href;  
var pathName=window.document.location.pathname;  
var pos=curWwwPath.indexOf(pathName);  
var localhostPaht=curWwwPath.substring(0,pos);  
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);  
console.log(localhostPaht+projectName);   
var projectPath=localhostPaht+projectName;


$(function(){
	$("#userId").val(getQueryString("userId"));
	$("#token").val(getQueryString("token"));
	$("#back").val(getQueryString("back"));
	$("#storeNo").val(getQueryString("storeNo"));
});

function popupDiv(div_id,val) {
	// 获取传入的DIV 
	var $div_obj = $("#" + div_id);
	// 计算机屏幕高度 
	var windowWidth = $(window).width();
	// 计算机屏幕长度 
	var windowHeight = $(window).height();
	// 取得传入DIV的高度 
	var popupHeight = $div_obj.height();
	// 取得传入DIV的长度 
	var popupWidth = $div_obj.width();

	// // 添加并显示遮罩层 
	$("<div id='bg'></div>").width(document.body.scrollWidth * 0.99)
		.height(document.body.scrollHeight * 0.99).click(function() {
			//hideDiv(div_id); 
		}).appendTo("body").fadeIn(200);
	$("#bg").css("display","block");
	// 显示弹出的DIV 
	$div_obj.css({
		"position": "fixed"
	}).animate({
		left: windowWidth / 2 - popupWidth / 2,
		top: windowHeight / 2 - popupHeight / 2,
		opacity: "show"
	}, "slow");
	$("#showText").text(val);
}
/*隐藏弹出DIV*/
function hideDiv(div_id) {
	$("#bg").remove();
	$("#bg").css("display","none");
	console.log("背景对象："+$("#bg"));
	$("#" + div_id).animate({
		left: 0,
		top: 0,
		opacity: "hide"
	}, "slow");
}
//获取URL中的参数
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
} 

function validate(){
	var obj=$("#phone").val();
    var reg = /^[0-9]*$/;
    if(!reg.test(obj)){
    	$("#phone").val("");
    }
};

function judgeProv(prov){
	var provinceArr=["北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆","香港","澳门","台湾"];
	for (var i = 0; i < provinceArr.length; i++) {
		if(prov.indexOf(provinceArr[i])>0){
			return false;
		}
	}
	return true;
}