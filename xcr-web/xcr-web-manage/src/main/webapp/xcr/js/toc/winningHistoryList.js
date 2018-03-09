var isWin = $("#isWin option:selected").attr("value");

var queryParams = function (params) {

    var isWin = $("#isWin option:selected").attr("value");
    var code = $("#code").val();
    var param = {
        pageStart: params.offset,
        pageSize: params.limit,
        isWin: isWin,
        code:code
    };
    return param;
};

var responseHandler = function (e) {
    if (e.data && e.data.length > 0) {
        return {"rows": e.data, "total": e.total};
    }
    else {
        return {"rows": [], "total": 0};
    }

};

var liveUpFormatter = function (value, row, index) {
    var typex = '';
    if (row.isWin == 1) {
        typex = "是";
    } else {
        typex = "否";
    }
    return ['<h4><i></i>' + typex + '</h4>'
    ].join('');
};

var timeFormatter=function(value, row, index){
	 if (row.createTime == null) {
	        return [].join('');
	    } else {
	        return ['<h4><i></i>' + getLocalTime(row.createTime) + '</h4>'
	        ].join('');
	    }
};


var versionDescriptionFormatter = function (value, row, index) {

    return ['<h4><div style="width:350px;  white-space: nowrap;overflow: hidden;text-overflow:ellipsis;">' + row.description + '</div></h4>'
    ].join('');

};
$('#loginLog-table').bootstrapTable({
    url: "/xcr/settingList.htm",
    method: "post",                     //使用post请求到服务器获取数据
    dataType: "json",
    contentType: 'application/json',
    toolbar: "#toolbar",                //一个jQuery 选择器，指明自定义的toolbar 例如:#toolbar, .toolbar.
    uniqueId: "id",                    //每一行的唯一标识，一般为主键列
    cache: false,                       // 不缓存
    height: 650,
    striped: true,                      // 隔行加亮
    queryParamsType: "limit",           //设置为"undefined",可以获取pageNumber，pageSize，searchText，sortName，sortOrder
                                        //设置为"limit",符合 RESTFul 格式的参数,可以获取limit, offset, search, sort, order
    queryParams: queryParams,
    sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
    pagination: true,                   //是否显示分页（*）
    strictSearch: true,
    showColumns: false,                  //是否显示所有的列
    showRefresh: true,                  //是否显示刷新按钮
    showToggle: true,                    //是否显示详细视图和列表视图
    clickToSelect: true,                //是否启用点击选中行
    minimumCountColumns: 2,          //最少允许的列数 clickToSelect: true, //是否启用点击选中行
    pageNumber: 1,                   //初始化加载第一页，默认第一页
    pageSize: 10,                    //每页的记录行数（*）
    pageList: [10],     //可供选择的每页的行数（*）
    paginationPreText: "上一页",
    paginationNextText: "下一页",
    paginationFirstText: "First",
    paginationLastText: "Last",
    responseHandler: responseHandler,

    columns: columns = [
        {
            field: 'id',
            title: '抽奖序号',
            align: 'center',
            sortable: false,
        }, {
            field: 'userId',
            title: '用户ID',
            align: 'center',
            sortable: false   //本列不可以排序
        }, {
            field: 'userName',
            title: '用户名',
            align: 'center',
            sortable: false   
        }
        , {
            field: 'realName',
            title: '真实姓名',
            align: 'center',
            sortable: false
        }, {
            field: 'isWin',
            title: '是否中奖',
            align: 'center',
            formatter: liveUpFormatter,
            sortable: false
        }, {
            field: 'phoneNumber',
            title: '手机号码',
            align: 'center',
            sortable: false
        }, {
            field: 'cityName',
            title: '所在城市',
            align: 'center',
            sortable: false
        },
        {
            field: 'orderId',
            title: '订单编号',
            align: 'center',
            sortable: false
        },{
            field: 'createTime',
            title: '参与时间',
            align: 'center',
            formatter: timeFormatter,
            sortable: false
        },{
            field: 'deviceType',
            title: '抽奖设备',
            align: 'center',
            sortable: false
        }],
    onLoadSuccess: function (data) { //加载成功时执行
        console.log(data);
    },
    onLoadError: function (res) { //加载失败时执行
        console.log(res);
    }
});

//查询
function searchResult() {
    var isWin = $("#isWin option:selected").attr("value");
    var code = $("#code").val();
    var options = $('#loginLog-table').bootstrapTable('refresh', {
        query: {
        	pageStart: 0,
            isWin: isWin,
            code:code
        }
    });
}

function getLocalTime(nS) {
    var d = new Date(nS).format("yyyy-MM-dd HH:mm:ss");
    return d;
}


