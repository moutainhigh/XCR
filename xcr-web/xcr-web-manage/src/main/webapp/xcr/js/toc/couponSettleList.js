var queryParams = function (params) {

    var orderState = $("#orderState option:selected").val();
    var companyName = $("#companyName").val();
    var shopNo = $("#shopNo").val();
    var storeNo = $("#storeNo").val();
    var storeName = $("#storeName").val();
    var startdate = $("#startdate").val();
    var enddate = $("#enddate").val();

    var param = {
        pageIndex: params.offset,
        pageSize: params.limit,
        orderState: orderState,
        companyName: companyName,
        shopNo: shopNo,
        storeNo: storeNo,
        storeName: storeName,
        startdate: startdate,
        enddate: enddate
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
    var detailUrl = "/xcr/toc/settleDetail/" + row.settleNo + "/" + 2 + "/List.htm";
    return [
        '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>详情</h4></a>'
    ].join('');
};

$('#loginLog-table').bootstrapTable({
    url: "/xcr/toc/couponSettle/List.htm",
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
            field: 'settleNo',
            title: '结算单号',
            align: 'center',
            visible: false,
            sortable: false
        },{
            field: 'subCompName',
            title: '所属子公司',
            align: 'center',
            sortable: false
        }, {
            field: 'shopNo',
            title: '加盟商编号',
            align: 'center',
            sortable: false   
        }, {
            field: 'storeNo',
            title: '门店编号',
            align: 'center',
            sortable: false
        }, {
            field: 'storeName',
            title: '门店名称',
            align: 'center',
            sortable: false
        }, {
            field: 'discountAmount',
            title: '优惠券金额',
            align: 'center',
            sortable: false
        },{
            field: 'settleAmount',
            title: '结算金额',
            align: 'center',
            sortable: false
        }, {
            field: 'paidDate',
            title: '交易日期',
            align: 'center',
            sortable: false
        }, {
            field: 'settleTime',
            title: '结算时间',
            align: 'center',
            sortable: false
        }, {
            field: 'settleStatus',
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
    var companyName = $("#companyName").val();
    var shopNo = $("#shopNo").val();
    var storeNo = $("#storeNo").val();
    var storeName = $("#storeName").val();
    var startdate = $("#startdate").val();
    var enddate = $("#enddate").val();

    if (startdate != null && startdate != "" && enddate != null && enddate != "") {
        if (startdate > enddate) {
            $("#timeSearchError").modal("toggle");
            $("#endDay").val(startdate);
            return false;
        }
    }

    var options = $('#loginLog-table').bootstrapTable('refresh', {
        query: {
            pageIndex: 0,
            orderState: orderState,
            companyName: companyName,
            shopNo: shopNo,
            storeNo: storeNo,
            storeName: storeName,
            startdate: startdate,
            enddate: enddate
        }
    });
}

