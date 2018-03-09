// 普通任务列表 
var queryParams = function (params) {
    var param = {
        pageIndex: params.offset,
        pageSize: params.limit
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

    console.log(row);
    var row_data = {};
    row_data.id = row.id;
    row_data.title = row.title.replace(/\s/g, "");
    row_data.imageUrl = row.imageUrl;
    row_data.msgUrl = row.msgUrl;
    row_data.status = (row.status == "未发布" ? "0" : "1");
    row_data.type = row.type;
    row_data.createUid = (row.createUid);
    row_data.createTime = row.createTime;
    row_data.modifyUid = row.modifyUid;
    row_data.pushType = (row.pushType);
    row_data.shopNo = row.pushTo;

    var json_data = JSON.stringify(row_data);
    //1.消息未发布时，可以编辑、删除、发布；
    //2.消息已发布时，只能进行删除，不可以编辑、发布，两个按钮置灰
    if (row.status == "已发布") {
        return [
            '<button class="btn btn-group btn-sm rightSize detailBtn edit" type="button" disabled><i class="fa fa-edit"></i> 编辑</button>',
            '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" value=' + json_data + ' onclick="removePush(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
            '<button class="btn btn-group btn-sm rightSize detailBtn release" type="button" disabled style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
        ].join('');
    }
    if (row.status == "未发布") {
        return [
            '<button class="btn btn-vk btn-sm rightSize detailBtn edit" type="button" value=' + json_data + ' onclick="editPush(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
            '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" value=' + json_data + ' onclick="removePush(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
            '<button class="btn btn-vk btn-sm rightSize detailBtn release" type="button" value=' + json_data + ' onclick="envelopePush(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
        ].join('');
    }
    return [
        '<button class="btn btn-vk btn-sm rightSize detailBtn edit" type="button" disabled value=' + json_data + ' onclick="editPush(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
        '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" disabled value=' + json_data + ' onclick="removePush(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
        '<button class="btn btn-vk btn-sm rightSize detailBtn release" type="button" disabled value=' + json_data + ' onclick="envelopePush(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
    ].join('');
};


$('#push-table').bootstrapTable({
    url: "/xcr/message/getMessageList.htm",
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
    showColumns: true,                  //是否显示所有的列
    showRefresh: true,                  //是否显示刷新按钮
    showToggle: true,                    //是否显示详细视图和列表视图
    clickToSelect: true,                //是否启用点击选中行
    minimumCountColumns: 2,          //最少允许的列数 clickToSelect: true, //是否启用点击选中行
    pageNumber: 1,                   //初始化加载第一页，默认第一页
    pageSize: 10,                    //每页的记录行数（*）
    pageList: [10, 25, 50, 100],     //可供选择的每页的行数（*）
    paginationPreText: "上一页",
    paginationNextText: "下一页",
    paginationFirstText: "First",
    paginationLastText: "Last",
    responseHandler: responseHandler,

    columns: columns = [
        {
            field: 'id',
            title: '序号',
            align: 'center',
            sortable: false
        }, {
            field: 'title',
            title: '标题',
            align: 'center',
            sortable: false
        }, {
            field: 'status',
            title: '状态',
            align: 'center',
            sortable: false
        }, {
            field: 'modifyTime',
            title: '最后更新时间',
            align: 'center',
            sortable: false
        }, {
            field: 'pushTo',
            title: '发送对象',
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


// 用于编辑数据
var editUrl = "/xcr/msgEdit.htm";
function editPush(json_data) {
    console.log("编辑" + json_data);
    var row = $.parseJSON(json_data);
    var param = {
        "id": row.id
    };
    console.log(row.status);
    if (row.status == 0) {
        $.ajax({
            url: "/xcr/message/getMessageById.htm",
            type: "POST",
            data: param,
            success: function (data) {
                console.log("data:" + JSON.stringify(data));
                var input = {
                    returnUrl: editUrl,
                    id: row.id,
                    title: data.title,
                    imageUrl: data.imageUrl,
                    msgUrl: data.msgUrl,
                    pushType: data.pushType,
                    shopNo: row.shopNo,
                    pushTo: data.pushTo,
                    contentFrom: data.contentFrom,
                    contentHtml: data.contentHtml,
                    areaStr:data.areaStr
                };
                console.log("input:" + input);
                load2Html(editUrl, input);
            }
        });
    } else if (row.status == 1) {
        $("#cannot").modal("toggle");
    }
}

//删除
function removePush(json_data) {
    var row = $.parseJSON(json_data);
    var param = {
        "id": row.id
    };
    win.confirm('删除确认', '确认删除消息吗？', function (r) {
        if (r === true) {
            $.ajax({
                url: "/xcr/message/deleteMessage.htm",
                type: "POST",
                data: param,
                success: function (data) {
                    if (data == true) {
                        $("#success").modal("toggle");
                        //刷新页面
                        $('#success').on('hidden.bs.modal', function () {
                            $('#push-table').bootstrapTable(
                                "refresh",
                                {
                                    url: "/xcr/message/getMessageList.htm"
                                }
                            );
                        })
                    } else {
                        $("#fail").modal("toggle");
                    }
                }
            });
        }
    });
}


//发布
function envelopePush(json_data) {
    var row = $.parseJSON(json_data);

    var param = {
        "id": row.id
    };

    $.ajax({
        url: "/xcr/message/getMessageById.htm",
        type: "POST",
        data: param,
        success: function (data) {
            console.log("data:" + JSON.stringify(data));
            var areaStr = data.areaStr;
            var areaArr = [];
            if(areaStr != null && areaStr != ''){
                areaArr = areaStr.split(",");
            }
            var input = {
                returnUrl: editUrl,
                id: row.id,
                title: data.title,
                imageUrl: data.imageUrl,
                msgUrl: data.msgUrl,
                status: "1",
                type: row.type,
                createUid: row.createUid,
                createTime: row.createTime,
                modifyUid: row.modifyUid,
                modifyTime: Date.parse(new Date()),
                releasesTime: Date.parse(new Date()),
                pushType: data.pushType,
                shopNo: row.shopNo,
                pushTo: data.pushTo,
                areaArr: areaArr,
                contentFrom: data.contentFrom,
                contentHtml: data.contentHtml
            };
            console.log("input:" + JSON.stringify(input));
            if (row.status == 0) {
                win.confirm(' 发布确认', '发布后立即生效，确认发布消息吗？', function (r) {
                    if (r === true) {
                        $.ajax({
                            url: "/xcr/message/releaseMessage.htm",
                            type: "POST",
                            data: JSON.stringify(input),
                            contentType: "application/json; charset=utf-8",
                            success: function (data) {
                                if (data) {
                                    $("#sendSuccess").modal("toggle");
                                    //刷新页面
                                    $('#sendSuccess').on('hidden.bs.modal', function () {
                                        $('#push-table').bootstrapTable(
                                            "refresh",
                                            {
                                                url: "/xcr/message/getMessageList.htm"
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
            } else if (row.status == 1) {
                $("#cannot").modal("toggle");
            }
        }
    });
}
function load2Html(path, data) {
    if (path.substring(0, $.ctx.length) == $.ctx) {
        $("#pushListTab").load(path);
    } else {
        $("#pushListTab").load($.ctx + path);
    }
    window.customData = data;

}