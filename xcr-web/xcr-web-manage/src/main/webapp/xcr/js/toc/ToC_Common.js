$(function(){
	jeDate({
		dateCell:"#startDate",
		format:"YYYY-MM-DD",
		festival:true, //是否显示节日
		isinitVal:false,
		isTime:false, //isClear:false,
		minDate:"2010-01-01",
		okfun:function(val){}
	})
	jeDate({
		dateCell:"#endDate",
		format:"YYYY-MM-DD",
		festival:true, //是否显示节日
		isinitVal:false,
		isTime:false, //isClear:false,
		minDate:"2010-01-01",
		okfun:function(val){}
	})
})
