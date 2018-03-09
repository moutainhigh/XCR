$(function(){
	jeDate({
			dateCell:"#startDate1",
			format:"YYYY-MM-DD hh:mm:ss",
			festival:true, //是否显示节日
			isinitVal:false,
			isTime:true, //isClear:false,
			minDate:"2010-01-01 00:00:00",
			okfun:function(val){}
		})
		jeDate({
			dateCell:"#endDate1",
			format:"YYYY-MM-DD hh:mm:ss",
			festival:true, //是否显示节日
			isinitVal:false,
			isTime:true, //isClear:false,
			minDate:"2010-01-01 00:00:00",
			okfun:function(val){}
		})
});
