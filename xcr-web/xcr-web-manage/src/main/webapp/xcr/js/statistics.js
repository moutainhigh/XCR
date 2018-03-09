/**
 * 
 */
function onChangeLi(objParam){
	console.log("============:"+objParam.value);
	$(".ulStyle").children().each(function(i,n){
     	var obj = $(n);
		obj.children().eq(0).css("color","#000000");
	 	if(obj.children().eq(0).val()==objParam.value){
			obj.children().eq(0).css("color","#099fb8");//弹出子元素标签
		}
    });
	if(objParam.value == "筛选"){
		var startDate=new Date($("#txtEndDate1").val().replace("-", "/").replace("-", "/"));
		var endDate=new Date($("#txtEndDate2").val().replace("-", "/").replace("-", "/"));
		if(!$("#txtEndDate1").val()=="" &&  !$("#txtEndDate2").val()==""){
			if(endDate>new Date() || startDate>new Date()){
				win.alert("日期错误","结束日期与开始日期不能大于当前日期",null);
				return false;
			}else{
				if(endDate<startDate){
					win.alert("日期错误","结束日期不能小于开始日期",null);
					return false;
				}else{
					console.log("new Date()-startDate="+(new Date()-startDate)/1000/3600/24);
					if((new Date()-startDate)>31*24*60*60*1000){
						win.alert("日期错误","最大只能查询离当前日期30天内的数据",null);
						return false;
					}else{
						console.log("endDate:"+$("#txtEndDate2").val()+"  getDay(0):"+getDay(0));
						console.log("startDate:"+$("#txtEndDate1").val()+"  getDay(-1):"+getDay(-1));
						if($("#txtEndDate2").val()==getDay(0) && $("#txtEndDate1").val()==getDay(0)){
							$("#today").css("color","#099fb8");
						}else if($("#txtEndDate2").val()==getDay(-1) && $("#txtEndDate1").val()==getDay(-1)){
							$("#yestoday").css("color","#099fb8");
						}else if($("#txtEndDate2").val()==getDay(-1) && $("#txtEndDate1").val()==getDay(-7)){
							$("#dayBeforeSeven").css("color","#099fb8");
						}else if($("#txtEndDate2").val()==getDay(-1) && $("#txtEndDate1").val()==getDay(-30)){
							$("#dayBefore30Day").css("color","#099fb8");
						}
						$("#statisticsOperateContent").attr("src","statisticsOperate.htm?startDate="+$("#txtEndDate1").val().replace("-", "/").replace("-", "/")+"&endDate="+$("#txtEndDate2").val().replace("-", "/").replace("-", "/"));
					}
				}
			}
		}else{
			win.alert("日期错误","开始日期与结束日期不能为空",null);
			return false;
		}
	}else{
		console.log("====执行固定时间条件筛选====");
		switch(objParam.value){
			case "今日":
				$("#txtEndDate1").val(getDay(0));
				$("#txtEndDate2").val(getDay(0));
				$("#statisticsOperateContent").attr("src","statisticsOperate.htm?param=1");
				break;
			case "昨天":
				$("#txtEndDate1").val(getDay(-1));
				$("#txtEndDate2").val(getDay(-1));
				$("#statisticsOperateContent").attr("src","statisticsOperate.htm?param=2");
				break;
			case "近7天":
				$("#txtEndDate1").val(getDay(-7));
				$("#txtEndDate2").val(getDay(-1));
				$("#statisticsOperateContent").attr("src","statisticsOperate.htm?param=3");
				break;
			case "近30天":
				$("#txtEndDate1").val(getDay(-30));
				$("#txtEndDate2").val(getDay(-1));
				$("#statisticsOperateContent").attr("src","statisticsOperate.htm?param=4");
				break;
			default:
				break;
		}
	}
}

function getDay(day) {
	var today = new Date();
	var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
	today.setTime(targetday_milliseconds); //注意，这行是关键代码    
	var tYear = today.getFullYear();
	var tMonth = today.getMonth();
	var tDate = today.getDate();
	tMonth = doHandleMonth(tMonth + 1);
	tDate = doHandleMonth(tDate);
	return tYear + "-" + tMonth + "-" + tDate;
}

function doHandleMonth(month) {
	var m = month;
	if(month.toString().length == 1) {
		m = "0" + month;
	}
	return m;
}

