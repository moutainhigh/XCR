var type = $("#type option:selected").attr("value");
var isLiveUp = $("#isLiveUp option:selected").attr("value");
var state = $("#state option:selected").attr("value");
var versionCode = $("#versionCode").attr("value");
var startDay = $("#startDay").attr("value");
var endDay = $("#endDay").attr("value");

var queryParams = function (params) {

    var type = $("#type option:selected").attr("value");
    var isLiveUp = $("#isLiveUp option:selected").attr("value");
    var state = $("#state option:selected").attr("value");
    var versionCode = $("#versionCode").val();
    var startDay = $("#startDay").val();
    var endDay = $("#endDay").val();

    console.log("pageIndex:" + params.offset);
    console.log("pageSize:" + params.limit);
    console.log("type:" + type);
    console.log("startDay:" + startDay);
    console.log("endDay:" + endDay);
    console.log("versionCode:" + versionCode);
    console.log("isLiveUp:" + isLiveUp);

    var param = {
        pageIndex: params.offset,
        pageSize: params.limit,
        state: state,
        versionCode: versionCode,
        isLiveUp: isLiveUp,
        type: type,
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
    // var detailUrl = "/xcr/order/" + row.orderNo + "/detail.htm";
    var id = row.id;
    var state = row.state;
    var url1 = '';
    var url2 = '';
    var url3 = '';
    if (state == 0) {
        url1 = '<div  onclick="updateState(' + id + ')"  class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>发布</h4></div>';
        url2 = '<div  onclick="delateVersion(' + id + ')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>作废</h4></div>';
        url3 = '<div  onclick="findDetailVersion(' + id + ')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>查看</h4></div>';
    } else {
        url3 = '<div  onclick="findDetailVersion(' + id + ')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>查看</h4></div>';
    }

    return [

        /* '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>详情</h4></a>',

         '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>查看</h4></a>',
         '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>修改</h4></a>',
         '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>删除</h4></a>'*/
        url1, url2, url3
    ].join('');
};


var typeFormatter = function (value, row, index) {
    var typex = '';
    if (row.type == 1) {
        typex = "安卓";
    } else if (row.type == 0) {
        typex = "ios";
    } else if (row.type == 2) {
        typex = "供应链资源包";
    }
    return ['<h4><i></i>' + typex + '</h4>'
    ].join('');
};

var liveUpFormatter = function (value, row, index) {
    var typex = '';
    if (row.isLiveUp == 1) {
        typex = "是";
    } else {
        typex = "否";
    }
    return ['<h4><i></i>' + typex + '</h4>'
    ].join('');
};

var stateFormatter = function (value, row, index) {
    var typex = '';
    if (row.state == 1) {
        typex = "已发布";
    } else {
        typex = "未发布";
    }
    return ['<h4><i></i>' + typex + '</h4>'
    ].join('');
};

var timeFormatter = function (value, row, index) {
    if (row.publishTime == null) {
        return [].join('');
    } else {
        return ['<h4><i></i>' + getLocalTime(row.publishTime) + '</h4>'
        ].join('');
    }
};

var versionDescriptionFormatter = function (value, row, index) {

    return ['<h4><div style="width:350px;  white-space: nowrap;overflow: hidden;text-overflow:ellipsis;">' + row.description + '</div></h4>'
    ].join('');

}
$('#loginLog-table').bootstrapTable({
    url: "/xcr/versionList.htm",
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
            field: 'type',
            title: '用户名',
            align: 'center',
            sortable: false,
            formatter: typeFormatter
        }, {
            field: 'versionCode',
            title: '用户电话',
            align: 'center',
            sortable: false   //本列不可以排序
        }, {
            field: 'code',
            title: '注册时间',
            align: 'center',
            sortable: false   
        }
        , {
            field: 'pulishTime',
            title: '渠道编号',
            align: 'center',
            formatter: timeFormatter,
            sortable: false
        }, {
            field: 'isLiveUp',
            title: '所属子公司',
            align: 'center',
            formatter: liveUpFormatter,
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
    var type = $("#type option:selected").attr("value");
    var isLiveUp = $("#isLiveUp option:selected").attr("value");
    var state = $("#state option:selected").attr("value");
    var versionCode = $("#versionCode").val();
    var startDay = $("#startDay").val();
    var endDay = $("#endDay").val();
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
            state: state,
            versionCode: versionCode,
            isLiveUp: isLiveUp,
            type: type,
            startDay: startDay,
            endDay: endDay
        }
    });
}

function getLocalTime(nS) {
    var d = new Date(nS).format("yyyy-MM-dd HH:mm:ss");
    return d;
}


function resetInput() {
    window.customData = null;
    $("#menu1").empty();
    $.ajax({
        url: "/xcr/versionManage.htm",
        type: "get",
        success: function (data) {
            $("#menu1").html(data);
            $(".modal-backdrop").remove();
        }
    });
}


function turnVersion(flag, versionCode, description, isLiveUp, apkUrl, id, state, code) {
    window.customData = null;
    $.ajax({
        url: "/xcr/versionAdd.htm",
        type: "get",
        success: function (data) {
            $("#loginLogTab").html(data);
            $("#type").val(flag);
            if (flag == 0) {
                $("#anzhuangbao").hide();
                $("#typeName").html("IOS");
            } else {
                if (flag == 1) {
                    $("#typeName").html("安卓");
                }
                if (flag == 2) {
                    $("#typeName").html("供应链");
                }
                if (apkUrl != null) {
                    $("#apkUrl").val(apkUrl);
                    $("#file").attr("value", apkUrl);
                    $("#apk").html(apkUrl);
                }
            }
            if (isLiveUp != null) {
                $("input[name=isLiveUp][value=" + isLiveUp + "]").attr("checked", true);
            }
            if (code != null) {
                $("#code").val(code);
            }
            if (id != null) {
                $("#versionId").val(id);
            }

            if (versionCode != null) {
                $("#versionCode").val(versionCode);
            }
            if (description != null) {
                $("#description").val(description);
            }

            if (state == 1) {
                $("#saveTask").hide();
                $("#cancleTask").hide();
                $("#file").hide();
                $("#filebtn").hide();
            }

        }
    });


}

function updateState(id) {

    $.ajax({
        url: "/xcr/version/updateState.htm",
        data: {id: id, state: 1},
        type: "post",
        success: function (data) {
            if (data != true) {
                alert("发布失败");
                return false;
            } else {
                resetInput();
            }
        }
    });

}


function delateVersion(id) {
    $.ajax({
        url: "/xcr/version/updateState.htm",
        data: {id: id, isDelate: 1},
        type: "post",
        success: function (data) {
            if (data != true) {
                alert("操作失败");
                return false;
            } else {
                resetInput();
            }
        }
    });
}


function saveVersion() {
    console.log("aaaaa");

    var formData = new FormData();
    var type = $("#type").val();
    var isLiveUp = $('input[name="isLiveUp"]:checked').attr("value");
    var versionCode = $("#versionCode").val();
    var description = $("#description").val();
    var id = $("#versionId").val();
    var apkUrl = $("#apkUrl").val();
    var code = $("#code").val();
    if (isLiveUp == null) {
        alert("请选择更新类型");
        return false;
    }
    if (versionCode == null || versionCode == "") {
        alert("请选择版本号");
        return false;
    }
    if (code == null || code == "") {
        alert("请选择序号");
        return false;
    }
    if (description == null || description == "") {
        alert("请添加描述信息");
        return false;
    }
    if (type != 0) {
        if ($("#file")[0].files[0] == null & apkUrl == "") {
            alert("请上传安装包");
            return false;
        }
    }
    try {
        formData.append("file", $("#file")[0].files[0]);
    } catch (err) {

    }
    formData.append("type", type);
    formData.append("isLiveUp", isLiveUp);
    formData.append("versionCode", versionCode);
    formData.append("description", description);
    formData.append("code", code);
    $("#uploadApk").modal("show");
    if (id == "" || id == null) {
        $.ajax({
            url: "/xcr/insertVersion.htm",
            // data : {type:type,versionCode:versionCode,isLiveUp:isLiveUp,description:description,file:file},
//	            data : $(formData).serialize(),
            data: formData,
            // 告诉jQuery不要去处理发送的数据
            processData: false,
            // 告诉jQuery不要去设置Content-Type请求头
            contentType: false,
            type: "post",
            success: function (data) {
                $("#uploadApk").modal("hide");
                if (data != true) {
                    alert("操作失败");
                    return false;
                } else {
                    resetInput();
                }
            }
        });
    } else {
        formData.append("id", id);
        formData.append("apkUrl", apkUrl);
        $.ajax({
            url: "/xcr/updateVersion.htm",
//		        	data : $(formData).serialize(),
            data: formData,
            // 告诉jQuery不要去处理发送的数据
            type: 'POST',
            processData: false,
            // 告诉jQuery不要去设置Content-Type请求头
            contentType: false,
            type: "post",
            success: function (data) {
                if (data != true) {
                    alert("操作失败");
                    return false;
                } else {
                    $("#uploadApk").modal("hide");
                    resetInput();
                }
            }
        });
    }
}


function findDetailVersion(id) {
    $.ajax({
        url: "/xcr/versionDetail.htm",
        data: {id: id},
        type: "post",
        success: function (data) {
            turnVersion(data.type, data.versionCode, data.description, data.isLiveUp, data.apkUrl, data.id, data.state, data.code);

        }
    });


}

$(function(){
	$("#file").change(function(){
	$("#apk").html($("#file").val());
	});
});

