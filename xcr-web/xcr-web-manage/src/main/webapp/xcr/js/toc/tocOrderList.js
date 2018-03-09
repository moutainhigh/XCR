var orderState = $("#orderState option:selected").val();
var payChannel = $("#payChannel option:selected").val();
var orderNum = $("#orderNum").val();
var shopCode = $("#shopCode").val();
var subCompany = $("#subCompany").val();
var startDay = $("#startDay").val();
var endDay = $("#endDay").val();
var shippingWay = $("#shippingWay option:selected").val();

var queryParams = function (params) {

    var orderState = $("#orderState option:selected").val();
    var payChannel = $("#payChannel option:selected").val();
    var orderNum = $("#orderNum").val();
    var shopCode = $("#shopCode").val();
    var subCompany = $("#subCompany").val();
    var startDay = $("#startDay").val();
    var endDay = $("#endDay").val();
    var shippingWay = $("#shippingWay option:selected").val();

    console.log("pageIndex:" + params.offset);
    console.log("pageSize:" + params.limit);
    console.log("orderState:" + orderState);
    console.log("payChannel:" + payChannel);
    console.log("orderNum:" + orderNum);
    console.log("shopCode:" + shopCode);
    console.log("subCompany:" + subCompany);
    console.log("startDay:" + startDay);
    console.log("endDay:" + endDay);
    console.log("shippingWay:" + shippingWay);

    var param = {
        pageIndex: params.offset,
        pageSize: params.limit,
        orderState: orderState,
        payChannel: payChannel,
        orderNum: orderNum,
        shopCode: shopCode,
        subCompany: subCompany,
        startDay: startDay,
        endDay: endDay,
        shippingWay: shippingWay
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
    return [
        '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>详情</h4></a>'
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
            title: '订单编号',
            align: 'center',
            sortable: false
        }, {
            field: 'createTime',
            title: '下单时间',
            align: 'center',
            sortable: false   //本列不可以排序
        }, {
            field: 'state',
            title: '订单状态',
            align: 'center',
            sortable: false
        }, {
            field: 'amountTotal',
            title: '订单总金额',
            align: 'center',
            sortable: false
        }, {
            field: 'discount',
            title: '优惠金额',
            align: 'center',
            sortable: false
        }, {
            field: 'amount',
            title: '订单实付金额',
            align: 'center',
            sortable: false
        }, {
            field: 'deliveryFee',
            title: '运费',
            align: 'center',
            sortable: false
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
            field: 'payway',
            title: '支付渠道',
            align: 'center',
            sortable: false
        }, {
            field: 'shippingWay',
            title: '配送方式',
            align: 'center',
            sortable: false
        }, {
            field: 'phoneNumber',
            title: '用户电话',
            align: 'center',
            sortable: false
        }, {
            field: 'updateTime',
            title: '最后更新时间',
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
    var shippingWay = $("#shippingWay option:selected").val();

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
            endDay: endDay,
            shippingWay: shippingWay
        }
    });
}

//导出订单
function orderExport() {
    var orderState = $("#orderState option:selected").val();
    var payChannel = $("#payChannel option:selected").val();
    var orderNum = $("#orderNum").val();
    var shopCode = $("#shopCode").val();
    var subCompany = $("#subCompany").val();
    var startDay = $("#startDay").val();
    var endDay = $("#endDay").val();
    var shippingWay = $("#shippingWay option:selected").val();

    var form = $("<form>");       // 定义一个form表单
    form.attr('style', 'display:none');   // 在form表单中添加查询参数
    form.attr('target', '');
    form.attr('method', 'post');// 使用POST方式提交
    form.attr('action', "/xcr/outorder/export.htm");

    var input1 = $("<input type='hidden'  />");
    input1.attr('name', 'state');
    input1.attr('value', orderState);

    var input2 = $("<input type='hidden'  />");
    input2.attr('name', 'companyName');
    input2.attr('value', subCompany);

    var input3 = $("<input type='hidden'  />");
    input3.attr('name', 'shopId');
    input3.attr('value', shopCode);

    var input4 = $("<input type='hidden'  />");
    input4.attr('name', 'payway');
    input4.attr('value', payChannel);

    var input5 = $("<input type='hidden'  />");
    input5.attr('name', 'orderNo');
    input5.attr('value', orderNum);

    var input6 = $("<input type='hidden'  />");
    input6.attr('name', 'start');
    input6.attr('value', startDay);

    var input7 = $("<input type='hidden'  />");
    input7.attr('name', 'end');
    input7.attr('value', endDay);

    var input8 = $("<input type='hidden'  />");
    input8.attr('name', 'shippingWay');
    input8.attr('value', shippingWay);


    $('body').append(form);  //将表单放置在web中
    form.append(input1);       //将查询参数控件添加到表单上
    form.append(input2);
    form.append(input3);       //将查询参数控件添加到表单上
    form.append(input4);
    form.append(input5);       //将查询参数控件添加到表单上
    form.append(input6);
    form.append(input7);       //将查询参数控件添加到表单上
    form.append(input8);
    form.submit();

}

