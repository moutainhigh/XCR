var cancelState = $("#cancelState option:selected").val(); //退货状态
var payWay = $("#payWay option:selected").val(); //支付渠道
var cancelId = $("#cancelId").val(); //退单编号
var shopId = $("#shopId").val(); //门店编号
var companyName = $("#companyName").val(); //所属子公司
var startTime = $("#startTime").val();
var endTime = $("#endTime").val();

var queryParams = function (params) {

    var cancelState = $("#cancelState option:selected").val(); //退货状态
    var payWay = $("#payWay option:selected").val(); //支付渠道
    var cancelId = $("#cancelId").val(); //退单编号
    var shopId = $("#shopId").val(); //门店编号
    var companyName = $("#companyName").val(); //所属子公司
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();

    var param = {
        pageIndex: params.offset,
        pageSize: params.limit,
        cancelState: cancelState,
        payWay: payWay,
        cancelId: cancelId,
        shopId: shopId,
        companyName: companyName,
        startTime: startTime,
        endTime: endTime
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
    var detailUrl = "/xcr/salesReturn/" + row.cancelId + "/detail.htm";
    return [
        '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>详情</h4></a>'
    ].join('');
};

$('#loginLog-table').bootstrapTable({
    url: "/xcr/toc/salesReturn/list.htm",
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
            field: 'cancelId',
            title: '退单编号',
            align: 'center',
            sortable: false
        }, {
            field: 'historyOrderNo',
            title: '原订单编号',
            align: 'center',
            sortable: false   //本列不可以排序
        }, {
            field: 'shopId',
            title: '门店编号',
            align: 'center',
            sortable: false
        }, {
            field: 'companyName',
            title: '所属子公司',
            align: 'center',
            sortable: false
        }, {
            field: 'cancelAmount',
            title: '应退金额',
            align: 'center',
            sortable: false
        }, {
            field: 'createTime',
            title: '申请时间',
            align: 'center',
            sortable: false
        }, {
            field: 'payWay',
            title: '支付渠道',
            align: 'center',
            sortable: false
        }, {
            field: 'cancelState',
            title: '处理状态',
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
function searchSalesResult() {
    var cancelState = $("#cancelState option:selected").val(); //退货状态
    var payWay = $("#payWay option:selected").val(); //支付渠道
    var cancelId = $("#cancelId").val(); //退单编号
    var shopId = $("#shopId").val(); //门店编号
    var companyName = $("#companyName").val(); //所属子公司
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();

    if (startTime != null && startTime != "" && endTime != null && endTime != "") {
        if (startTime > endTime) {
            $("#timeSearchError").modal("toggle");
            $("#endTime").val(startTime);
            return false;
        }
    }
    console.log("cancelState:" + cancelState);
    console.log("payWay:" + payWay);
    console.log("cancelId:" + cancelId);
    console.log("shopId:" + shopId);
    console.log("companyName:" + companyName);
    console.log("startTime:" + startTime);
    console.log("endTime:" + endTime);


    var options = $('#loginLog-table').bootstrapTable('refresh', {
        query: {
            pageIndex: 0,
            cancelState: cancelState,
            payWay: payWay,
            cancelId: cancelId,
            shopId: shopId,
            companyName: companyName,
            startTime: startTime,
            endTime: endTime
        }
    });
}

//导出订单
function orderExport() {
    var cancelState = $("#cancelState option:selected").val(); //退货状态
    var payWay = $("#payWay option:selected").val(); //支付渠道
    var cancelId = $("#cancelId").val(); //退单编号
    var shopId = $("#shopId").val(); //门店编号
    var companyName = $("#companyName").val(); //所属子公司
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();

    var form = $("<form>");       // 定义一个form表单
    form.attr('style', 'display:none');   // 在form表单中添加查询参数
    form.attr('target', '');
    form.attr('method', 'post');// 使用POST方式提交
    form.attr('action', "xcr/toc/salesReturn/export.htm");

    var input1 = $("<input type='hidden'  />");
    input1.attr('name', 'cancelState');
    input1.attr('value', cancelState);

    var input2 = $("<input type='hidden'  />");
    input2.attr('name', 'payWay');
    input2.attr('value', payWay);

    var input3 = $("<input type='hidden'  />");
    input3.attr('name', 'cancelId');
    input3.attr('value', cancelId);

    var input4 = $("<input type='hidden'  />");
    input4.attr('name', 'shopId');
    input4.attr('value', shopId);

    var input5 = $("<input type='hidden'  />");
    input5.attr('name', 'companyName');
    input5.attr('value', companyName);

    var input6 = $("<input type='hidden'  />");
    input6.attr('name', 'startTime');
    input6.attr('value', startTime);

    var input7 = $("<input type='hidden'  />");
    input7.attr('name', 'endTime');
    input7.attr('value', endTime);


    $('body').append(form);  //将表单放置在web中
    form.append(input1);       //将查询参数控件添加到表单上
    form.append(input2);
    form.append(input3);       //将查询参数控件添加到表单上
    form.append(input4);
    form.append(input5);       //将查询参数控件添加到表单上
    form.append(input6);
    form.append(input7);       //将查询参数控件添加到表单上
    form.submit();

}

