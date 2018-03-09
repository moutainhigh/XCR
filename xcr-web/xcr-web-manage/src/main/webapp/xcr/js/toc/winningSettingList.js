
var queryParams = function (params) {


    var param = {
        pageStart: params.offset,
        pageSize: params.limit,
        sham: true
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
    var id = row.id;
    var url1 = '';  
        url1 = '<button value=' + id +' onclick="deleteWinningUser(this.value)"  class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>删除</h4></button>';
    return [
            url1
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
            field: 'userName',
            title: '用户名',
            align: 'center',
            sortable: false,
        }, {
            field: 'phoneNumber',
            title: '手机号',
            align: 'center',
            sortable: false   //本列不可以排序
        }, {
            field: 'cityName',
            title: '所在城市',
            align: 'center',
            sortable: false   
        }
      , {
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
   

    var options = $('#loginLog-table').bootstrapTable('refresh', {
        query: {
            pageStart: 1,
            sham:true
        }
    });
}



function deleteWinningUser(id) {
	var code = $("#code").val();
    $.ajax({
        url: "/xcr/deleteWinningUser.htm",
        data: {userId: id},
        type: "post",
        success: function (data) {
            if (data != true) {
                alert("操作失败");
                return false;
            } else {
            	setWinningList(code);
            }
        }
    });
}


function saveWinningUser() {
	var code = $("#code").val();
    var userName = $("#userName").val();
    var address = $("#address").val();
    var phoneNum = $("#phoneNum").val();
    if (userName == null || userName=="") {
        alert("请输入用户名");
        return false;
    }
    if (address == null || address == "") {
        alert("请输入所在区域");
        return false;
    }
    if (phoneNum == null || phoneNum == "") {
        alert("请输入联系电话");
        return false;
    }
        $.ajax({
            url: "/xcr/addWinningUser.htm",
            data: {userName:userName,
            			address:address,
            			phoneNum:phoneNum,
            			code:code},
            type: "post",
            success: function (data) {
                if (data != true) {
                    alert("操作失败");
                    return false;
                } else {
                	setWinningList(code);
                	$(".modal-backdrop").remove();
                }
            }
        });
}

function AddWinningVIP(){
	$("#winningSettingAdd").modal("toggle");
}


