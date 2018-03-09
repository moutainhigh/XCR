var queryParams = function (params) {
	var operationCenterId=$.trim($("#operationCenterId option:selected").val());
	var franchinessId=$.trim($("#franchinessId").val());
	var franchiseeName=$.trim($("#franchiseeName").val());
	var o2oFlag=$.trim($("#o2oFlag option:selected").val());
	var storeId=$.trim($("#storeId").val());
	var storeName=$.trim($("#storeName").val());

	console.log("====o2oFlag:"+o2oFlag);

	var param = {
			operationCenterId:operationCenterId,
			franchinessId:franchinessId,
			franchiseeName:franchiseeName,
			o2oFlag:o2oFlag,
			storeId:storeId,
			storeName:storeName,
			pageNo: params.offset,
			pageSize: params.limit,
	};
	return param;
};

var responseHandler = function (e) {
	console.log(e);
	if (e.data && e.data.length > 0) {
		return {"rows": e.data, "total": e.total};
	}
	else {
		return {"rows": [], "total": 0};
	}

};
var isEnableFormat = function (value, row, index){
	console.log("deliveryModeFormat:"+value);
	if(parseInt(value)==1){
		return "<label style='color:#28FF28;'>使用中</label>";
	}else if(parseInt(value)==0){
		return "<label>已停用<label>";
	}
}

var dateFormat  = function(value,row,index){
	console.log("最后更新时间："+value);
	if(value==null || value==""){
		return "";
	}else{
		var startTime=new Date(value).format('yyyy-MM-dd  hh:mm:ss');
		return startTime;
	}
}

var actionFormatter = function (value, row, index) {//赋予的参数
	var oprHtml='<div class="action" style="cursor:pointer;float:left;width:70px;"><a onclick="scMng(1,'+row.id+',\''+row.groupName+'\')"><h5><i class="fa fa-pencil-square-o"></i> 修改</h5></a></div>';
	if(parseInt(value)==1){
		oprHtml+='<div class="action" style="cursor:pointer;float:left;width:80px;"><a onclick="scSynState('+row.id+',0)"><h5><i class="fa fa-pencil-square-o"></i>停用</h5></a></div>';
	}else if(parseInt(value)==0){
		oprHtml+='<div class="action" style="cursor:pointer;float:left;width:80px;"><a onclick="scSynState('+row.id+',1)"><h5><i class="fa fa-pencil-square-o"></i>启用</h5></a></div>';
	}
	oprHtml+='<div class="action" style="cursor:pointer;float:left;width:80px;"><a onclick="scSynState('+row.id+',2)"><h5><i class="fa fa-pencil-square-o"></i>删除</h5></a></div>';
	return oprHtml;
};

$('#loginLog-table').bootstrapTable({
	url: "/xcr/sc/scGrouplist.htm",
	method: "post",                     //使用post请求到服务器获取数据
	dataType: "json",
	contentType: 'application/json',
	toolbar: "#toolbar",                //一个jQuery 选择器，指明自定义的toolbar 例如:#toolbar, .toolbar.
	uniqueId: "id",                    //每一行的唯一标识，一般为主键列
	cache: false,                       // 不缓存
	height: 800,
	striped: true,                      // 隔行加亮
	queryParamsType: "limit",           //设置为"undefined",可以获取pageNumber，pageSize，searchText，sortName，sortOrder
	//设置为"limit",符合 RESTFul 格式的参数,可以获取limit, offset, search, sort, order
	queryParams: queryParams,
	sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
	pagination: true,                   //是否显示分页（*）
	strictSearch: true,
	showColumns: false,                  //是否显示所有的列
	showRefresh: true,                  //是否显示刷新按钮
	showToggle: false,                    //是否显示详细视图和列表视图
	clickToSelect: true,                //是否启用点击选中行
	minimumCountColumns: 2,          //最少允许的列数 clickToSelect: true, //是否启用点击选中行
	pageNumber: 1,                   //初始化加载第一页，默认第一页
	pageSize: 25,                    //每页的记录行数（*）
	pageList: [10, 25, 50, 100],     //可供选择的每页的行数（*）
	paginationPreText: "上一页",
	paginationNextText: "下一页",
	paginationFirstText: "First",
	paginationLastText: "Last",
	responseHandler: responseHandler,
	columns: columns = [
	                    {
	                    	field: 'addrCode',
	                    	title: '模块位置',
	                    	align: 'center',
	                    	width: '10%',
	                    	sortable: false
	                    }, {
	                    	field: 'groupName',
	                    	title: '推荐主题',
	                    	width: '30%',
	                    	align: 'center',
	                    	sortable: false
	                    }, {
	                    	field: 'lastModifyTime',
	                    	title: '最后更改时间',
	                    	align: 'center',
	                    	width: '30%',
	                    	sortable: false,
	                    	formatter: dateFormat //自定义方法，添加操作按钮
	                    },{
	                    	field: 'isEnable',
	                    	title: '当前状态',
	                    	align: 'center',
	                    	width: '10%',
	                    	sortable: false,
	                    	formatter: isEnableFormat //自定义方法，添加操作按钮
	                    }, {
	                    	field: 'isEnable',
	                    	title: '操作',
	                    	align: 'center',
	                    	valign: 'middle',
	                    	width:  '20%',
	                    	formatter: actionFormatter //自定义方法，添加操作按钮
	                    }],
	                    onLoadSuccess: function (data) { //加载成功时执行
	                    	console.log("scGroupList成功数据："+data);
	                    },
	                    onLoadError: function (res) { //加载失败时执行
	                    	console.log("scGroupList失败数据："+res);
	                    }
});
/**
 * 新增或者修改商品广告
 * @param val
 * @param groupId
 * @param groupName
 */
function scMng(val,groupId,groupName){
	console.log("操作的值为："+val);
	javascript:loadHtml("/xcr/sc/scMng.htm?method="+val+"&groupId="+groupId);
}
function scSynState(id,state){
	switch(state){
	case 1:
		win.confirm("推荐商品操作","确定启用吗？",function (r) {
	        if (r === true) {
	        	scSynStateImpl(id,state);
	        }	
		});
		break;
	case 0:
		win.confirm("推荐商品操作","确定停用吗？",function (r) {
	        if (r === true) {
	        	scSynStateImpl(id,state);
	        }	
		});
		break;
	case 2:
		win.confirm("推荐商品操作","确定删除吗？",function (r) {
	        if (r === true) {
	        	scSynStateImpl(id,state);
	        }	
		});
		break;
	}
}
function scSynStateImpl(id,state){
	$.post("/xcr/sc/scSynState.htm",{"id":id,"state":state},function(data){
		console.log("js--->scSynState同步状态响应数据为："+data);
		var json=JSON.parse(data);
		if(json.success){
			loadHtml('xcr/sc/scGroup.htm');
		}else{
			win.alert("错误提示","修改失败，请重试...",null);
		}
	});
}

