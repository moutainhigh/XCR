var queryParams = function (params) {
    var orderState = $("#orderState option:selected").val();
    var payChannel = $("#payChannel option:selected").val();
    var orderNum = $("#orderNum").val();
    var shopCode = $("#shopCode").val();
    var subCompany = $("#subCompany").val();
    var startDay = $("#startDay").val();
    var endDay = $("#endDay").val();
    var param = {
        pageIndex: params.offset,
        pageSize: params.limit,
        orderState: orderState,
        payChannel: payChannel,
        orderNum: orderNum,
        shopCode: shopCode,
        subCompany: subCompany,
        startDay: startDay,
        endDay: endDay
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
var actionFormatter = function (value, row, index) {//赋予的参数
    var detailUrl = "/xcr/order/" + row.orderNo + "/detail.htm";
    var addUrl = "/xcr/toc/redPacketActivityAdd.htm";
    return [
        '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>查看</h4></a>',
        '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>修改</h4></a>',
        '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>删除</h4></a>'
    ].join('');
};

$('#loginLog-table').bootstrapTable({
    url: "/xcr/order/list.htm",
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
    pageSize: 25,                    //每页的记录行数（*）
    pageList: [10, 25, 50, 100],     //可供选择的每页的行数（*）
    paginationPreText: "上一页",
    paginationNextText: "下一页",
    paginationFirstText: "First",
    paginationLastText: "Last",
    responseHandler: responseHandler,
    columns: columns = [
        {
            field: 'orderNo',
            title: '活动名称',
            align: 'center',
            sortable: false
        }, {
            field: 'createTime',
            title: '时间范围',
            align: 'center',
            sortable: false
        }, {
            field: 'state',
            title: '红包总金额',
            align: 'center',
            sortable: false
        }, {
            field: 'state',
            title: '创建时间',
            align: 'center',
            sortable: false
        }, {
            field: 'state',
            title: '状态',
            align: 'center',
            sortable: false
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: actionFormatter //自定义方法，添加操作按钮
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
    var orderState = $("#orderState option:selected").val();
    var payChannel = $("#payChannel option:selected").val();
    var orderNum = $("#orderNum").val();
    var shopCode = $("#shopCode").val();
    var subCompany = $("#subCompany").val();
    var startDay = $("#startDay").val();
    var endDay = $("#endDay").val();

    console.log("orderState:" + orderState);
    console.log("payChannel:" + payChannel);
    console.log("orderNum:" + orderNum);
    console.log("shopCode:" + shopCode);
    console.log("subCompany:" + subCompany);
    console.log("startDay:" + startDay);
    console.log("endDay:" + endDay);

    if (startDay != null && startDay != "" && endDay != null && endDay != "") {
        if (startDay > endDay) {
            $("#timeSearchError").modal("toggle");
            $("#endDay").val(startDay);
            return false;
        }
    }

    var options = $('#loginLog-table').bootstrapTable('refresh', {
        query: {
            pageIndex: 0,
            orderState: orderState,
            payChannel: payChannel,
            orderNum: orderNum,
            shopCode: shopCode,
            subCompany: subCompany,
            startDay: startDay,
            endDay: endDay
        }
    });
}

// 用于编辑数据
function editMission(json_data) {
    var row = $.parseJSON(json_data);
    var orderId = row.id;
    var detailUrl = "/xcr/toc/" + orderId + "/activityAdd.htm";
    javascript:loadHtml(detailUrl);
}

//使用详情
function removeMission(json_data) {
    console.log(json_data);
    var row = $.parseJSON(json_data);
    var orderId = row.id;
    var detailUseUrl = "/xcr/toc/" + orderId + "/discountUseList.htm";
    javascript:loadHtml(detailUseUrl);
}


//发布
function envelopeMission(json_data) {
    console.log(json_data);
    var row = $.parseJSON(json_data);
    var status = row.status;
    var param = {
        "missionId": row.id
    };
    if (status == "INIT") {
        win.confirm('发布确认', '确认发布任务吗？？', function (r) {
            if (r === true) {
                $.ajax({
                    url: "/xcr/mission/publish.htm",
                    type: "POST",
                    data: param,
                    success: function (data) {
                        if (data == true) {
                            $("#success").modal("toggle");
                            //刷新页面
                            $('#success').on('hidden.bs.modal', function () {
                                $('#loginLog-table').bootstrapTable(
                                    "refresh",
                                    {
                                        url: "/xcr/mission/queryBy.htm"
                                    }
                                );
                            })
                        } else {
                            $("#fail").modal("toggle");
                        }
                    }
                });
            }
        })
    } else {
        $("#release").modal("toggle");
    }
}