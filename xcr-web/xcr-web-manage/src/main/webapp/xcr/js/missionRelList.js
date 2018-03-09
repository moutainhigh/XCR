//关联任务列表 
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
var operateFormatter = function (value, row, index) {//赋予的参数

    var row_data = {};
    row_data.id = row.id;
    row_data.relMissionName = row.missonRelatedName.replace(/\s/g, "");
    row_data.missonRelatedDescription = row.missonRelatedDescription;
    row_data.status = (row.status == "未发布" ? "INIT" : row.status == "已发布" ? "PUBLISH" : "CANCEL");
    row_data.details = row.details;

    var json_data = JSON.stringify(row_data);
    //任务组已发布，不可编辑、发布，可以删除，可以查看详情，但是不能修改；任务未发布，可以编辑、删除、发布，
    if (row.status == "已发布") { //已发布
        return [
            '<button class="btn btn-group btn-sm rightSize detailBtn edit" type="button" disabled value=' + json_data + ' onclick="editRelMission(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
            '<button class="btn btn-group btn-sm rightSize detailBtn remove" type="button" disabled value=' + json_data + ' onclick="removeRelMission(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
            '<button class="btn btn-group btn-sm rightSize detailBtn release" type="button" disabled value=' + json_data + ' onclick="envelopeRelMission(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
        ].join('');
    }
    if (row.status == "未发布") { //下架/取消
        return [
            '<button class="btn btn-vk btn-sm rightSize detailBtn edit" type="button" value=' + json_data + ' onclick="editRelMission(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
            '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" value=' + json_data + ' onclick="removeRelMission(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
            '<button class="btn btn-vk btn-sm rightSize detailBtn release" type="button" value=' + json_data + ' onclick="envelopeRelMission(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
        ].join('');
    }
    return [
        '<button class="btn btn-vk btn-sm rightSize detailBtn edit" type="button" disabled value=' + json_data + ' onclick="editRelMission(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
        '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" disabled value=' + json_data + ' onclick="removeRelMission(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
        '<button class="btn btn-vk btn-sm rightSize detailBtn release" type="button" disabled value=' + json_data + ' onclick="envelopeRelMission(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
    ].join('');
};

$('#sockctOutput-table').bootstrapTable({
    url: "/xcr/mission/queryRelBy.htm",
    method: "post",                     //使用post请求到服务器获取数据
    dataType: "json",
    contentType: 'application/json',
    toolbar: "#toolbar",                //一个jQuery 选择器，指明自定义的toolbar 例如:#toolbar, .toolbar.
    uniqueId: "id",                    //每一行的唯一标识，一般为主键列
    height: 650,                       //固定高度值，不加该属性为动态自适应
    cache: false,                       // 不缓存
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
            title: '任务id',
            align: 'center',
            sortable: false
        }, {
            field: 'missonRelatedName',
            title: '关联任务组',
            align: 'center',
            sortable: false   //本列不可以排序
        }, {
            field: 'status',
            title: '任务状态',
            align: 'center',
            sortable: false
        }, {
            field: 'lastModifyTime',
            title: '最后更新时间',
            align: 'center',
            sortable: false
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: operateFormatter //自定义方法，添加操作按钮
        }],
    onLoadSuccess: function (data) { //加载成功时执行
        console.log(data);
    },
    onLoadError: function (res) { //加载失败时执行
        console.log(res);
    }
});

var relationMissionEditUrl = "/xcr/missionRelAdd.htm";
var showUrl = "/xcr/missionRelShow.htm";
//编辑
function editRelMission(json_data) {
    var row = $.parseJSON(json_data);
    var input = {
        returnUrl: relationMissionEditUrl,
        "relMissionId": row.id,
        "relMissionName": row.relMissionName,
        "relMissionDetail": row.missonRelatedDescription,
        "status": row.status,
        "details": row.details,
        "lastModifyTime": row.lastModifyTime
    };
    if (row.status == "INIT") {
        if (relationMissionEditUrl.substring(0, $.ctx.length) == $.ctx) {
            $("#socketOutputTab").load(relationMissionEditUrl);
        } else {
            $("#socketOutputTab").load($.ctx + relationMissionEditUrl);
        }
        window.customData = input;
    } else if (row.status == "PUBLISH") {
        //$("#release").modal("toggle");
        if (showUrl.substring(0, $.ctx.length) == $.ctx) {
            $("#socketOutputTab").load(showUrl);
        } else {
            $("#socketOutputTab").load($.ctx + showUrl);
        }
        window.customData = input;
    }
}

//删除
function removeRelMission(json_data) {
    var row = $.parseJSON(json_data);
    var status = row.status;
    win.confirm('系统提示', '确定删除吗？删除不可恢复！', function (r) {
        if (r === true) {
            $.ajax({
                url: "/xcr/mission/removeRel.htm",
                type: "POST",
                data: "relatedId=" + row.id,
                success: function (data) {
                    if (data == 1) {
                        $("#success").modal("toggle");
                        //刷新页面
                        $('#success').on('hidden.bs.modal', function () {
                            $('#sockctOutput-table').bootstrapTable(
                                "refresh",
                                {
                                    url: "/xcr/mission/queryRelBy.htm"
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

//发布任务
function envelopeRelMission(json_data) {
    var row = $.parseJSON(json_data);
    console.log("in envelopeRelMission ...");
    console.log("row:" + row);
    var status = row.status;
    if (status == "INIT") {
        win.confirm('发布确认', '确认发布任务吗？发布后不能删除！', function (r) {
            if (r === true) {
                $.ajax({
                    url: "/xcr/mission/publicRel.htm",
                    type: "POST",
                    data: "relatedId=" + row.id,
                    success: function (data) {
                        if (data == 1) {
                            $("#success").modal("toggle");
                            //刷新页面
                            $('#success').on('hidden.bs.modal', function () {
                                $('#sockctOutput-table').bootstrapTable(
                                    "refresh",
                                    {
                                        url: "/xcr/mission/queryRelBy.htm"
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
