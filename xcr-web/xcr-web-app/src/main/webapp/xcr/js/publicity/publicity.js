/**
 * 
 */
var myslider = new iSlider({
	wrap : '#wrap',
	item : '.item',
	onslide : function(index) {
	}
});
var srcPath=projectPath;
if(projectPath.indexOf("/html")>0){
	srcPath=localhostPaht;
}
/**
 * 跳转加盟登记页面
 */
function joinEnroll(){
	console.log("/html/joinEnroll.html");
	location.href = srcPath+"/html/joinEnroll.html";
}
/**
 * 跳转APP下载页面
 */
function appDownload(){
	console.log(srcPath+"/html/appActDownload.html");
	location.href = srcPath+"/html/appActDownload.html";
}