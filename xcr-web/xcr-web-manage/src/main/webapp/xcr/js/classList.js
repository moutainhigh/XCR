//普通任务列表 
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

    var row_data = {};
    var rowname = $.trim(row.name);
    row_data.id = row.id;
    console.log("rowname:" + rowname);
    row_data.name = rowname.replace(/\s/g, "");
    row_data.icon = row.icon;
    // row_data.content = row.content;
    row_data.trainLength = row.trainLength;
    row_data.createUid = (row.createUid);
    row_data.status = (row.status == "未发布" ? "0" : "1");
    row_data.isMission = (row.isMission == "是" ? "1" : "0");

    console.log("row_data:" + row_data);
    var json_data = JSON.stringify(row_data);

    //若为学习任务，不能删除、编辑、发布、下架，按钮置灰
    if (row.isMission == "是") {
        return [
            '<button class="btn btn-group btn-sm rightSize detailBtn edit" type="button" disabled><i class="fa fa-edit"></i> 编辑</button>',
            '<button class="btn btn-group btn-sm rightSize detailBtn remove" type="button" disabled value=' + json_data + ' onclick="removeClass(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
            '<button class="btn btn-group btn-sm rightSize detailBtn release" type="button" disabled style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>',
            '<button class="btn btn-group btn-sm rightSize detailBtn under" type="button" disabled value=' + json_data + ' onclick="underClass(this.value)" style="margin-left: 10px;"><i class="fa fa-level-down"></i> 下架</button>'
        ].join('');
    }
    //若不是学习任务，若已发布，不能编辑、删除、发布，可以下架
    if (row.status == "已发布") {
        return [
            '<button class="btn btn-group btn-sm rightSize detailBtn edit" type="button" disabled value=' + row.id + ' onclick="editClass(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
            '<button class="btn btn-group btn-sm rightSize detailBtn remove" type="button" disabled value=' + json_data + ' onclick="removeClass(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
            '<button class="btn btn-group btn-sm rightSize detailBtn release" type="button" disabled value=' + json_data + ' onclick="envelopeClass(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>',
            '<button class="btn btn-vk btn-sm rightSize detailBtn under" type="button" value=' + json_data + ' onclick="underClass(this.value)" style="margin-left: 10px;"><i class="fa fa-level-down"></i> 下架</button>'
        ].join('');
    }
    //若未发布，可以编辑、删除、发布
    return [
        '<button class="btn btn-vk btn-sm rightSize detailBtn edit" type="button" value=' + row.id + ' onclick="editClass(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
        '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" value=' + json_data + ' onclick="removeClass(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
        '<button class="btn btn-vk btn-sm rightSize detailBtn release" type="button" value=' + json_data + ' onclick="envelopeClass(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>',
        '<button class="btn btn-group btn-sm rightSize detailBtn under" type="button" disabled value=' + json_data + ' onclick="underClass(this.value)" style="margin-left: 10px;"><i class="fa fa-level-down"></i> 下架</button>'
    ].join('');
};


$('#class-table').bootstrapTable({
    url: "/xcr/train/getTrainList.htm",
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
            field: 'name',
            title: '课堂名称',
            align: 'center',
            sortable: false
        }, {
            field: 'trainLength',
            title: '建议学习时长(min)',
            align: 'center',
            sortable: false
        }, {
            field: 'isMission',
            title: '学习任务',
            align: 'center',
            sortable: false
        }, {
            field: 'status',
            title: '课堂状态',
            align: 'center',
            sortable: false
        }, {
            field: 'modifyTime',
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

//编辑
var editUrl = "/xcr/trainAdd.htm";
function editClass(id) {
    console.log("id:" + id);
    var param = {
        "id": id
    };
    $.ajax({
        url: "/xcr/train/getTrainById.htm",
        type: "POST",
        data: param,
        success: function (data) {
            console.log("data:" + data);
            console.log("data.id:" + data.id);
            console.log("data.content:" + data.content);
            var input = {
                id: data.id,
                name: data.name,
                icon: data.icon,
                content: data.content,
                trainLength: data.trainLength,
                status: data.status,
                isMission: data.isMission
            };
            load4Html(editUrl, input);
        }
    });

}

//删除
function removeClass(json_data) {
    console.log(json_data);
    var row = $.parseJSON(json_data);
    console.log(row.status);
    var param = {
        "id": row.id,
        status: row.status,
        isMission: row.isMission
    };
    win.confirm('删除确认', '确认删除课堂吗？', function (r) {
        if (r === true) {
            $.ajax({
                url: "/xcr/train/deleteTrain.htm",
                type: "POST",
                data: param,
                success: function (data) {
                    if (data == true) {
                        $('#successModel').modal('toggle')
                        //刷新页面
                        $('#successModel').on('hidden.bs.modal', function () {
                            $('#class-table').bootstrapTable(
                                "refresh",
                                {
                                    url: "/xcr/train/getTrainList.htm"
                                }
                            );
                        })
                    } else {
                        $("#fail").modal("toggle");
                        $('#fail').on('hidden.bs.modal', function () {
                            $('#class-table').bootstrapTable(
                                "refresh",
                                {
                                    url: "/xcr/train/getTrainList.htm"
                                }
                            );
                        })
                    }
                }
            });
        }
    })
}

//发布
function envelopeClass(json_data) {
    console.log(json_data);
    var row = $.parseJSON(json_data);
    console.log(row.status);
    var param = {
        "id": row.id,
        "name": row.name,
        // "content": row.content,
        status: row.status,
        isMission: row.isMission
    };
    win.confirm('发布确认', '发布后立即生效，确认发布课堂吗？', function (r) {
        if (r === true) {
            $.ajax({
                url: "/xcr/train/releaseTrain.htm",
                type: "POST",
                data: JSON.stringify(param),
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    if (data.flag) {
                        $("#successModel").modal("toggle");
                        //刷新页面
                        $('#successModel').on('hidden.bs.modal', function () {
                            $('#class-table').bootstrapTable(
                                "refresh",
                                {
                                    url: "/xcr/train/getTrainList.htm"
                                }
                            );
                        })
                    } else {
                        $("#fail").modal("toggle");
                        //刷新页面
                        $('#fail').on('hidden.bs.modal', function () {
                            $('#class-table').bootstrapTable(
                                "refresh",
                                {
                                    url: "/xcr/train/getTrainList.htm"
                                }
                            );
                        })
                    }
                }
            });
        }
    })
}

//下架
function underClass(json_data) {
    console.log(json_data);
    var row = $.parseJSON(json_data);
    console.log(row.status);
    var param = {
        "id": row.id,
        "name": row.name,
        // "content": row.content.replace(/^/g, ">"),
        "isMission": row.isMission,
        "status": row.status,
        "trainLength": row.trainLength
    };
    win.confirm('下架确认', '确认下架此课堂吗？', function (r) {
        if (r === true) {
            $.ajax({
                url: "/xcr/train/underTrain.htm",
                type: "POST",
                data: JSON.stringify(param),
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    console.log("data.flag:" + data.flag);
                    console.log("data == true:" + data == true);
                    if (data == true) {
                        $("#successModel").modal("toggle");
                        //刷新页面
                        $('#successModel').on('hidden.bs.modal', function () {
                            $('#class-table').bootstrapTable(
                                "refresh",
                                {
                                    url: "/xcr/train/getTrainList.htm"
                                }
                            );
                        })
                    } else {
                        $("#fail").modal("toggle");
                        //刷新页面
                        $('#fail').on('hidden.bs.modal', function () {
                            $('#class-table').bootstrapTable(
                                "refresh",
                                {
                                    url: "/xcr/train/getTrainList.htm"
                                }
                            );
                        })
                    }
                }
            });
        }
    })
}

function load4Html(path, data) {
    if (path.substring(0, $.ctx.length) == $.ctx) {
        $("#classListTab").load(path);
    } else {
        $("#classListTab").load($.ctx + path);
    }
    window.customData = data;
}
