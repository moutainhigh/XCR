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
    console.log("value:" + value);
    console.log("row:" + row);
    console.log("index:" + index);
    console.log("row.status:" + row.status);

    var row_data = {};
    row_data.awards = row.awards;
    row_data.id = row.id;
    row_data.name = row.name.replace(/\s/g, "");
    row_data.type = (row.type == "日常任务" ? "DAILY" : row.type == "推荐任务" ? "RECOMMEND" : row.type == "学习任务" ? "STUDY" : "");
    row_data.awardType = (row.awardType == "现金奖励" ? "CASH" : row.awardType == "积分奖励" ? "SCORE" : row.awardType == "现金及积分" ? "CASH_AND_SCORE" : "NONE");
    row_data.needManualAudit = (row.needManualAudit == "人工审核");
    row_data.status = (row.status == "未发布" ? "INIT" : row.status == "已发布" ? "PUBLISH" : row.status == "每日任务-已发布" ? "PUBLISH" : "CANCEL");
    row_data.iconUrl = row.iconUrl;
    row_data.templateCode = row.templateCode;
    row_data.templateName = row.templateName;
    row_data.related = (row.related != "普通任务");
    // row_data.description = row.description;
    row_data.courseId = row.courseId;
    row_data.durationTimeStart = row.durationTimeStart;
    row_data.durationTimeEnd = row.durationTimeEnd;
    row_data.validTimeStart = row.validTimeStart;
    row_data.validTimeEnd = row.validTimeEnd;


    var json_data = JSON.stringify(row_data);
    var detailUrl = "xcr/mission/" + row.id + "/missionDetail.htm";
    //1:若任务类型为普通任务时，任务已发布，不可编辑、发布，可以删除；任务未发布，可以编辑、删除、发布
    //2:若任务类型为关联任务时，状态为未发布，则不可发布，可以编辑、删除；状态为已发布时，则不可编辑、发布，可以删除
    if (row.status == "已发布" || row.status == "每日任务-已发布") {
        if (row.related != "普通任务") { //如果是关联任务
            return [
                '<button class="btn btn-group btn-sm rightSize detailBtn edit" type="button" disabled value=' + json_data + ' onclick="editMission(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
                '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-vk btn-sm rightSize detailBtn" style="width: 60px;height: 30px;margin-left: 10px;"><i class="fa fa-search-plus"></i>详情</a>',
                '<button class="btn btn-group btn-sm rightSize detailBtn remove" type="button" disabled value=' + json_data + ' onclick="removeMission(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
                '<button class="btn btn-group btn-sm rightSize detailBtn release" type="button" disabled value=' + json_data + ' onclick="envelopeMission(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
            ].join('');
        }
        return [
            '<button class="btn btn-group btn-sm rightSize detailBtn edit" type="button" disabled><i class="fa fa-edit"></i> 编辑</button>',
            '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-vk btn-sm rightSize detailBtn" style="width: 60px;height: 30px;margin-left: 10px;"><i class="fa fa-search-plus"></i>详情</a>',
            '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" value=' + json_data + ' onclick="removeMission(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
            '<button class="btn btn-group btn-sm rightSize detailBtn release" type="button" disabled style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
        ].join('');

    }
    if (row.status == "未发布") {
        if (row.related != "普通任务") { //如果是关联任务
            return [
                '<button class="btn btn-vk btn-sm rightSize detailBtn edit" type="button" value=' + json_data + ' onclick="editMission(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
                '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-vk btn-sm rightSize detailBtn" style="width: 60px;height: 30px;margin-left: 10px;"><i class="fa fa-search-plus"></i>详情</a>',
                '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" value=' + json_data + ' onclick="removeMission(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
                '<button class="btn btn-group btn-sm rightSize detailBtn release" type="button" disabled value=' + json_data + ' onclick="envelopeMission(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
            ].join('');
        }
        return [
            '<button class="btn btn-vk btn-sm rightSize detailBtn edit" type="button" value=' + json_data + ' onclick="editMission(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
            '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-vk btn-sm rightSize detailBtn" style="width: 60px;height: 30px;margin-left: 10px;"><i class="fa fa-search-plus"></i>详情</a>',
            '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" value=' + json_data + ' onclick="removeMission(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
            '<button class="btn btn-vk btn-sm rightSize detailBtn release" type="button" value=' + json_data + ' onclick="envelopeMission(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
        ].join('');
    }
    return [
        '<button class="btn btn-vk btn-sm rightSize detailBtn edit" type="button" disabled value=' + json_data + ' onclick="editMission(this.value)"><i class="fa fa-edit"></i> 编辑</button>',
        '<a href="javascript:loadHtml(\'' + detailUrl + '\')" class="btn btn-vk btn-sm rightSize detailBtn" style="width: 60px;height: 30px;margin-left: 10px;"><i class="fa fa-search-plus"></i>详情</a>',
        '<button class="btn btn-vk btn-sm rightSize detailBtn remove" type="button" disabled value=' + json_data + ' onclick="removeMission(this.value)" style="margin-left: 10px;"><i class="fa fa-remove"></i> 删除</button>',
        '<button class="btn btn-vk btn-sm rightSize detailBtn release" type="button" disabled value=' + json_data + ' onclick="envelopeMission(this.value)" style="margin-left: 10px;"><i class="fa fa-envelope"></i> 发布</button>'
    ].join('');
};

window.actionEvents = {
    'click .edit': function (e, value, row, index) {
        console.log("进入编辑...");
        console.log("row:" + row);
        console.log("value:" + value);
        console.log("index:" + index);
        //修改操作
    },
    'click .remove': function (e, value, row, index) {
        console.log("进入删除...");
        console.log("row:" + row);
        console.log("value:" + value);
        console.log("index:" + index);
        //删除操作
    }
};

$('#loginLog-table').bootstrapTable({
    url: "/xcr/mission/queryBy.htm",
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
            title: '任务名称',
            align: 'center',
            sortable: false   //本列不可以排序
        }, {
            field: 'related',
            title: '任务类型',
            align: 'center',
            sortable: false
        }, {
            field: 'type',
            title: '任务分类',
            align: 'center',
            sortable: false
        }, {
            field: 'awardType',
            title: '任务奖励',
            align: 'center',
            sortable: false
        }, {
            field: 'needManualAudit',
            title: '审核类型',
            align: 'center',
            sortable: false
        }, {
            field: 'usefulTime',
            title: '有效期',
            align: 'center',
            sortable: false
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
var normalMissionEditUrl = "/xcr/missionAdd.htm";
function editMission(json_data) {

    console.log(json_data);

    var row = $.parseJSON(json_data);
    console.log(row);
    console.log(row.id);
    console.log(row.name);
    var param = {
        "id": row.id
    };
    $.ajax({
        url: "/xcr/mission/getMissionById.htm",
        type: "POST",
        data: param,
        success: function (data) {
            var description = data.description;
            var continueDate = data.continueDate;
            var facePayNumber = data.facePayNumber;
            var facePayAmount = data.facePayAmount;
            var buyAmount = data.buyAmount;
            console.log("description:" + description);
            console.log("continueDate:" + continueDate);
            console.log("facePayNumber:" + facePayNumber);
            console.log("facePayAmount:" + facePayAmount);
            console.log("buyAmount:" + buyAmount);

            var input = {
                returnUrl: normalMissionEditUrl,
                "awards": row.awards,
                "missionId": row.id,//任务id
                "missionName": row.name,//任务名
                "missionType": row.type,//任务分类
                "missionAwardType": "SCORE",//任务奖励类型
                "needManualAudit": row.needManualAudit,//审核类型
                "missionStatus": row.status,//任务状态
                "iconUrl": row.iconUrl,//图标url
                "templateCode": row.templateCode,
                "templateName": row.templateName,
                "isRelated": row.related,//任务类型
                "description": description,
                "courseId": row.courseId,
                "durationTimeStart": row.validTimeStart,
                "durationTimeEnd": row.validTimeEnd,
                "validTimeStart": row.durationTimeStart,
                "validTimeEnd": row.durationTimeEnd,
                "continueDate": continueDate,
                "facePayNumber": facePayNumber,
                "facePayAmount": facePayAmount,
                "buyAmount": buyAmount

            };
            if (row.status == "INIT") {
                if (normalMissionEditUrl.substring(0, $.ctx.length) == $.ctx) {
                    $("#loginLogTab").load(normalMissionEditUrl);
                } else {
                    $("#loginLogTab").load($.ctx + normalMissionEditUrl);
                }
                window.customData = input;
            } else if (row.status == "PUBLISH") {
                $("#release").modal("toggle");
            }
        }
    });
}

//删除
function removeMission(json_data) {
    console.log(json_data);
    var row = $.parseJSON(json_data);
    var related = row.related;
    var status = row.status;
    var param = {
        "missionId": row.id
    };
    //var r = $("#confirmcancel").modal("toggle");
    win.confirm('删除确认', '确认删除此任务吗？删除后立即生效！！', function (r) {
        if (r === true) {
            $.ajax({
                url: "/xcr/mission/remove.htm",
                type: "POST",
                data: param,
                success: function (data) {
                    if (data == 1) {
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
    });
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