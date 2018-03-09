var end = '';
var head = '';
var pageData = [];

$(document).ready(function () {
    changeGroupName();
    pageData = [];
    adverList();
});

function adverList() {
    $("#xcraddAdv").show();
    pageData = [];
    $.post("/xcr/advertisement/findAdvertise.htm", {}, function (data) {
        head = '<thead><tr>' +
            '<td style="width:5%">序号 </td>' +
            '<td>广告组</td>' +
            '<td style="display:none;">广告编号</td>' +
            '<td>轮播图名称 </td>' +
            '<td style="width:5%">图片</td>' +
            '<td style="width:20%">链接地址</td>' +
            '<td>状态</td>	' +
            '<td  style="width:10%">操作</td>	' +
            '</tr></thead><tbody>';
        var realdata = data.resultObject.data;
        for (var i = 0; i < realdata.length; i++) {
            var state = realdata[i].state;
            var caozuo = "";
            if (state == 0) {
                state = '<label style="color:red;">停用</label>';
                caozuo = '<button class="btn btn-vk btn-sm rightSize detailBtn remove update111" adver-id=' + i + '  data-state="2" data-id=' + realdata[i].id + '><i class="fa fa-remove"></i> 删除</button>'
                    + '<button class="btn btn-vk btn-sm rightSize detailBtn edit" data-id=' + realdata[i].id + ' style="margin-left:20px;"><i class="fa fa-edit"></i> 编辑</button>';
            } else if (state == 1) {
                state = '<label style="color:green;">启用</label>';
                caozuo = '<button class="btn btn-group btn-sm rightSize detailBtn remove update111" adver-id=' + i + '  data-state="2" data-id=' + realdata[i].id + ' disabled><i class="fa fa-remove"></i> 删除</button>'
                    + '<button class="btn btn-group btn-sm rightSize detailBtn edit" data-id=' + realdata[i].id + ' style="margin-left:20px;" disabled><i class="fa fa-edit"></i> 编辑</button>';
            }

            var info =
                '<tr >' +
                '<td id="XUHAO" style="width:5%">' + (i + 1) + ' </td>' +
                '<td id="REMAIND">' + realdata[i].remaind + '</td>' +
                '<td id="ID" style="display:none;">' + realdata[i].id + '</td>' +
                '<td id="PIC_NAME">' + realdata[i].picName + ' </td>' +
                '<td id="PIC_URL" style="width:5%"><p class="triggerPicShowEnlarge"  rel="' + realdata[i].picUrl + '" data-id=' + realdata[i].id + ' onmouseover="showEnlargeImg(this);" style="width:"><img style="width:70px;height: 40px;" src="' + realdata[i].picUrl + '"/></p></td>';
            if (realdata[i].activityUrl.length > 50) {
                info += '<td id="ACTIVITY_URL"  style="width:20%">' + realdata[i].activityUrl.substring(0, 50) + ' ... </td>';
            } else {
                info += '<td id="ACTIVITY_URL"  style="width:20%">' + realdata[i].activityUrl.substring(0, 50) + '</td>';
            }
            info += '<td id="ACTIVITY_STATE">' + state + '</td>	' +
                '<td id="CAOZUO"  style="width:10%">' + caozuo + '</td>	' +
                '</tr>';
            pageData.push(info);
        }
        end = '</tbody>';
        goPage(1, 10);
    });
};


function turnAdd(name, url, id, picUrl, groupId, remaind) {
    window.customData = null;
    $("#AddTable").empty();
    $.ajax({
        url: "/xcr/advertiseAdd.htm",
        type: "get",
        async: false,//************
        success: function (data) {
            $("#AddTable").html(data);
            if (name != null) {
                $("#AddTable #advertiseName").val(name);
            }
            if (url != null) {
                $("#advertiseUrl").val(url);
            }
            if (id != null) {
                $("#advertiseId").val(id);
            }
            if (picUrl != null) {
                $("#AddTable .icon").html('<img id="icon" class="preventImg_pre" src="' + picUrl + '" style="width:300px;height: 225px;margin-left: 10px;">');
            }
            if (groupId != null) {
                $("#editFlag").val(groupId);
                var type = '<option data-id= "' + groupId + '" value="' + remaind + '">' + remaind + '</option>';
                changeGroupName(type);

                $.ajax({
                    type: "post",
                    url: "/xcr/advertisement/findAdverGroupById.htm",
                    data: {id: groupId},
                    async: false,
                    success: function (data) {
                        var positionCode = data.resultObject.positionCode;
                        if (positionCode == "100") {
                            positionCode = "首页";
                        } else {
                            positionCode = "未开放位置";
                        }
                        $("#XcrAdverGroup").val(positionCode);
                    }
                });


//				$.post("/xcr/advertisement/findAdverGroupById.htm",{
//					id:groupId
//				},function(data){
//					var positionCode = data.resultObject.positionCode;
//					if(positionCode=="100"){
//						positionCode="首页";
//					}else{
//						positionCode="未开放位置";
//					}
//					$("#XcrAdverGroup").val(positionCode);
//				});


            }

        }
    });
}


function turnList() {
    window.customData = null;
    $("#AddTable").empty();
    $.ajax({
        url: "/xcr/advertise.htm",
        type: "get",
        async: false,  ///***************
        success: function (data) {
            $("#AddTable").html(data);
            pageData = [];
        }
    });
}


$("#advertiseList").on("click", ".edit", function () {
    var id = $(this).attr("data-id");
    $.post("/xcr/advertisement/findAdvertisementById.htm", {
        id: id
    }, function (data) {
        turnAdd(data.resultObject.picName, data.resultObject.activityUrl, data.resultObject.id, data.resultObject.picUrl, data.resultObject.groupId, data.resultObject.remaind);
    });
});


$("#advertiseList").on("click", ".update111", function () {
    var id = $(this).attr("data-id");
    var adverid = $(this).attr("adver-id");
    var state = $(this).attr("data-state");
//    var this_=$(this);
    $.post("/xcr/advertisement/updateAdvertisement.htm", {
        id: id, state: state
    }, function (data) {
        $.post("/xcr/advertisement/findAdvertise.htm", {}, function (data) {
            pageData = [];
            var realdata = data.resultObject.data;
            for (var i = 0; i < realdata.length; i++) {
                var state = realdata[i].state;
                var caozuo = "";
                if (state == 0) {
                    state = '<label style="color:red;">停用</label>';
                    caozuo = '<button class="btn btn-vk btn-sm rightSize detailBtn remove update111" adver-id=' + i + '  data-state="2" data-id=' + realdata[i].id + '><i class="fa fa-remove"></i> 删除</button>'
                        + '<button class="btn btn-vk btn-sm rightSize detailBtn edit" data-id=' + realdata[i].id + ' style="margin-left:20px;"><i class="fa fa-edit"></i> 编辑</button>';
                } else if (state == 1) {
                    state = '<label style="color:green;">启用</label>';
                    caozuo = '<button class="btn btn-group btn-sm rightSize detailBtn remove update111" adver-id=' + i + '  data-state="2" data-id=' + realdata[i].id + ' disabled><i class="fa fa-remove"></i> 删除</button>'
                        + '<button class="btn btn-group btn-sm rightSize detailBtn edit" data-id=' + realdata[i].id + ' style="margin-left:20px;" disabled><i class="fa fa-edit"></i> 编辑</button>';
                }
                var info =
                    '<tr >' +
                    '<td  style="width:5%">' + (i + 1) + ' </td>' +
                    '<td >' + realdata[i].remaind + '</td>' +
                    '<td style="display:none;">' + realdata[i].id + '</td>' +
                    '<td >' + realdata[i].picName + ' </td>' +
                    //'<td  style="width:5%"><img style="width:70px;height: 40px;" src="'+realdata[i].picUrl + '" /></a></td>'+
                    '<td id="PIC_URL" style="width:5%"><p class="triggerPicShowEnlarge"  rel="' + realdata[i].picUrl + '" data-id=' + realdata[i].id + ' onmouseover="showEnlargeImg(this);" style="width:"><img style="width:70px;height: 40px;" src="' + realdata[i].picUrl + '"/></p></td>';
                if (realdata[i].activityUrl.length > 50) {
                    info += '<td  style="width:20%">' + realdata[i].activityUrl.substring(0, 50) + ' ... </td>';
                } else {
                    info += '<td  style="width:20%">' + realdata[i].activityUrl.substring(0, 50) + '</td>';
                }
                info += '<td >' + state + '</td>	' +
                    '<td  style="width:10%">' + caozuo + '</td>	' +
                    '</tr>';
                pageData.push(info);
            }
            var n1 = (adverid - adverid % 10) / 10;
            goPage(n1 + 1, 10);
        });
    });
});


function changeGroupName(type) {
    console.log("type:" + type);
    $.ajax({
        type: "post",
        url: "/xcr/advertisement/findAdvertiseGroup.htm",
        async: false,
        success: function (data) {
            $('#GroupNameList').empty();
            var realdata = data.resultObject.data;
            var group = '';
            if (type == null) {
                group = '<option value="">-请选择-</option>';
            } else {
                group = type;
            }
            for (var i = 0; i < realdata.length; i++) {
                console.log("type");
                var group1 = '<option data-id= "' + realdata[i].id + '" value="' + realdata[i].groupName + '">' + realdata[i].groupName + '</option>';
                if (type != group1) {
                    group += '<option data-id= "' + realdata[i].id + '" value="' + realdata[i].groupName + '">' + realdata[i].groupName + '</option>';
                }
            }
            $('#GroupNameList').append(group);
        }
    });
}

function selectGroupInfo() {
    var id = $("#GroupNameList").find("option:selected").attr("data-id");

//	$.post("/xcr/advertisement/findAdverGroupById.htm",{
//		id:id
//	},function(data){
//		var positionCode = data.resultObject.positionCode;
//		if(positionCode=="100"){
//			positionCode="首页";
//		}else{
//			positionCode="未开放位置";
//		}
//		$("#XcrAdverGroup").val(positionCode);
//	});


    $.ajax({
        type: "post",
        url: "/xcr/advertisement/findAdverGroupById.htm",
        data: {id: id},
        async: false,
        success: function (data) {
            var positionCode = data.resultObject.positionCode;
            if (positionCode == "100") {
                positionCode = "首页";
            } else {
                positionCode = "未开放位置";
            }
            $("#XcrAdverGroup").val(positionCode);
        }
    });

}

function cancleAdvertise() {
    turnList();
}


function saveAdvertise() {
    $("#totalflag").val("init");
    $("#adverflag").val("init");
    //var sore=$("#sore").val();
    var picName = $("#advertiseName").val();
    var activityUrl = $("#advertiseUrl").val();
    var id = $("#advertiseId").val();
    var remaind = $("#GroupNameList option:selected").val();
    var groupId = $("#GroupNameList option:selected").attr("data-id");
    if (groupId == null || groupId == "") {
        alert("组不能为空");
        return false;
    }

//	$.post("/xcr/advertisement/validateTotalAdver.htm",{
//		id:groupId
//	},function(data){
//		var validate=data.resultObject;
//		if(validate>=5){
//			$("#totalflag").val("flag");	
//		}
//	});

    var groupFlagid = $("#editFlag").val();

    $.ajax({
        type: "post",
        url: "/xcr/advertisement/validateTotalAdver.htm",
        data: {id: groupId},
        async: false,
        success: function (data) {
            var validate = data.resultObject;
            if (groupFlagid == groupId) {
                if (validate > 5) {
                    $("#totalflag").val("flag");
                }
            }
            else {
                if (validate >= 5) {
                    $("#totalflag").val("flag");
                }
            }


        }
    });
    if ($("#totalflag").val() == "flag") {
        alert("组内广告不能超过5个");
        return false;
    }
//	$.post("/xcr/advertisement/findAdvertisementByName.htm",{
//		name:picName
//	},function(data){
//		var validate=data.resultObject.picName;
//		if(validate==picName){
//			$("#adverflag").val("flag");	
//		}
//	});

    $.ajax({
        type: "post",
        url: "/xcr/advertisement/findAdvertisementByName.htm",
        data: {name: picName},
        async: false,
        success: function (data) {
            var validate = data.resultObject;
            if (validate != null || validate != undefined) {
                $("#adverflag").val("flag");
            }
        }
    });


    if (id == null) {

        if ($("#adverflag").val() == "flag") {
            alert("广告不能同名");
            return false;
        }
    }

    if (picName == null || picName == "") {
        alert("广告名不能为空");
        return false;
    }
    var picUrl = "";
    try {
        picUrl = $(".preventImg_pre")[0]["src"];
        if (0 != picUrl.indexOf("http")) {
            alert("请等待图片上传服务器结束");
            return false;
        }
    } catch (err) {
        alert("请上传图片");
        return false;
    }
//	$.post("/xcr/advertisement/addAdvertisement.htm",{
//		//sore:sore,
//		id:id,
//		picName:picName,
//		picUrl:picUrl,
//		activityUrl:activityUrl,
//		remaind:remaind,
//		type:1,
//		groupId:groupId
//	},function(data){	
//		turnList();
//});


    $.ajax({
        type: "post",
        url: "/xcr/advertisement/addAdvertisement.htm",
        data: {
            id: id,
            picName: picName,
            picUrl: picUrl,
            activityUrl: activityUrl,
            remaind: remaind,
            type: 1,
            groupId: groupId
        },
        async: false,
        success: function (data) {
            turnList();
        }
    });


}


function adverGroupList() {
    $("#xcraddAdv").hide();
    $.post("/xcr/advertisement/findAdvertiseGroup.htm", {}, function (data) {
        pageData = [];
        head =
            '<thead><tr >' +
            '<td >序号 </td>' +
            '<td >广告组名称</td>' +
            '<td id="ID" style="display:none;">广告组编号</td>' +
            '<td ">位置</td>' +
            '<td ">状态 </td>' +
            '<td ">操作 </td>' +
            '</tr></thead><tbody>';
        var realdata = data.resultObject.data;
        for (var i = 0; i < realdata.length; i++) {
            var state = realdata[i].isEnable;
            var caozuo = "";
            if (state == 0) {
                state = '<label style="color:red;">停用</label>';
                caozuo = '<button class="btn btn-vk btn-sm rightSize detailBtn updateGroup" adverGroup-id=' + i + '  dataGroup-state="1" dataGroup-id=' + realdata[i].id + '><i class="fa fa-check-circle-o"></i> 启用</button>'
                    + '<button class="btn btn-vk btn-sm rightSize detailBtn remove updateGroup" adverGroup-id=' + i + '  dataGroup-state="2" dataGroup-id=' + realdata[i].id + ' style="margin-left:15px;"><i class="fa fa-times"></i> 删除</button>';
            } else if (state == 1) {
                state = '<label style="color:green;">启用</label>';
                caozuo = '<button class="btn btn-vk btn-sm rightSize detailBtn updateGroup" adverGroup-id=' + i + ' dataGroup-state="0" dataGroup-id=' + realdata[i].id + '><i class="fa fa-ban"></i> 停用</button>'
                    + '<button class="btn btn-vk btn-sm rightSize detailBtn remove updateGroup" adverGroup-id=' + i + '  dataGroup-state="2" dataGroup-id=' + realdata[i].id + ' style="margin-left:15px;"><i class="fa fa-times"></i> 删除</button>';
            }


            var position = "";
            if (realdata[i].positionCode == "100") {
                position = "首页";
            } else {
                position = "未开放广告";
            }
            var info =
                '<tr >' +
                '<td >' + (i + 1) + ' </td>' +
                '<td >' + realdata[i].groupName + '</td>' +
                '<td id="ID"  style="display:none;">' + realdata[i].id + '</td>' +
                '<td >' + position + ' </td>' +
                '<td >' + state + '</a></td>' +
                '<td >' + caozuo + '</a></td>' +
                '</tr>';
            pageData.push(info);
        }
        end = '</tbody>';
        goPage(1, 10);
    });
}

function saveAdvertiseGroup() {
    $("#flag").val("init");
    var groupName = $("#advertiseGroupName").val();
    var position = $("#position option:selected").val();
    if (groupName == null || groupName == "") {
        alert("组名不能为空");
        return false;
    }
    if (position == null || position == "") {
        alert("请选择位置");
        return false;
    }


    $.ajax({
        type: "post",
        url: "/xcr/advertisement/findAdvertisementGroupByName.htm",
        data: {name: groupName},
        async: false,
        success: function (data) {
            var validate = data.resultObject;
            if (validate != null || validate != undefined) {
                $("#flag").val("flag");
            }
        }
    });


    if ($("#flag").val() == "flag") {
        alert("广告组创建不能重名");
        return false;
    }


    $.ajax({
        type: "post",
        url: "/xcr/advertisement/addAdvertisementGroup.htm",
        data: {groupName: groupName, positionCode: position},
        async: false,
        success: function (data) {
            $("#groupCre").modal("hide");
            changeGroupName();

        }
    });


}

function ceshi() {
    $("#groupCre").modal("toggle");
}

function openLogin() {
    document.getElementById("win").style.display = "";
}
function closeLogin() {
    document.getElementById("win").style.display = "none";
}


$("#advertiseList").on("click", ".updateGroup", function () {
    var id = $(this).attr("dataGroup-id");
    var adverid = $(this).attr("adverGroup-id");
    var state = $(this).attr("dataGroup-state");
//    var this_=$(this);
    $.post("/xcr/advertisement/updateAdvertisementGroup.htm", {
        id: id, state: state, positionCode: 100
    }, function (data) {
        if (data.code == "302") {
            alert(data.errorMessage);
            return false;
        }
        $.post("/xcr/advertisement/findAdvertiseGroup.htm", {}, function (data) {
            pageData = [];
            var realdata = data.resultObject.data;
            for (var i = 0; i < realdata.length; i++) {
                var state = realdata[i].isEnable;
                var caozuo = "";
                if (state == 0) {
                    state = '<label style="color:red;">停用</label>';
                    caozuo = '<button class="btn btn-vk btn-sm rightSize detailBtn updateGroup" adverGroup-id=' + i + '  dataGroup-state="1" dataGroup-id=' + realdata[i].id + '><i class="fa fa-check-circle-o"></i> 启用</button>'
                        + '<button class="btn btn-vk btn-sm rightSize detailBtn remove updateGroup" adverGroup-id=' + i + '  dataGroup-state="2" dataGroup-id=' + realdata[i].id + ' style="margin-left:15px;"><i class="fa fa-times"></i> 删除</button>';
                } else if (state == 1) {
                    state = '<label style="color:green;">启用</label>';
                    caozuo = '<button class="btn btn-vk btn-sm rightSize detailBtn updateGroup" adverGroup-id=' + i + ' dataGroup-state="0" dataGroup-id=' + realdata[i].id + '><i class="fa fa-ban"></i> 停用</button>'
                        + '<button class="btn btn-vk btn-sm rightSize detailBtn remove updateGroup" adverGroup-id=' + i + '  dataGroup-state="2" dataGroup-id=' + realdata[i].id + ' style="margin-left:15px;"><i class="fa fa-times"></i> 删除</button>';
                }
                var positionCode = "";
                if (realdata[i].positionCode == "100") {
                    positionCode = "首页";
                } else {
                    positionCode = "未开放位置";
                }
                var info =
                    '<tr >' +
                    '<td >' + (i + 1) + ' </td>' +
                    '<td >' + realdata[i].groupName + '</td>' +
                    '<td id="ID"  style="display:none;">' + realdata[i].id + '</td>' +
                    '<td >' + positionCode + ' </td>' +
                    '<td >' + state + '</a></td>' +
                    '<td >' + caozuo + '</a></td>' +
                    '</tr>';
                pageData.push(info);
            }
            var n1 = (adverid - adverid % 10) / 10;
            goPage(n1 + 1, 10);
        });
    });
});


function goPage(currentPage, PageSize) {
    var Count = pageData.length;//记录条数
    PageSize = 10;//设置每页示数目
    var PageCount = Math.ceil(Count / PageSize);//计算总页数

    if (Count / PageSize > parseInt(Count / PageSize)) {
        PageCount = parseInt(Count / PageSize) + 1;
    } else {
        PageCount = parseInt(Count / PageSize);
    }
    var startRow = (currentPage - 1) * PageSize + 1;//开始显示的行
    var endRow = currentPage * PageSize;//结束显示的行
    endRow = (endRow > Count) ? Count : endRow;
    console.log(endRow);
    var tempStr1 = "共 " + Count + " 条记录    共 " + PageCount + " 页     当前第 " + currentPage + " 页";
    var tempStr = "";
    if (currentPage > 1) {
        tempStr += ' <a href=\"#\" selectPage=\"1\" onClick=\"goPage(' + (1) + ',' + PageSize + ')\"><input type="text" value="首页"  class="input" readonly/></a> ';
        tempStr += ' <a href=\"#\" selectPage="' + (currentPage - 1) + '" onClick=\"goPage(' + (currentPage - 1) + ',' + PageSize + ')\"><input type="text" value="<上一页"  class="input" readonly/></a> '
    } else {
        tempStr += ' <input type="text" value="首页"  class="input" readonly/> <input type="text" value="<上一页"  class="input" readonly/> ';
    }

    if (currentPage < PageCount) {
        tempStr += ' <a href=\"#\" selectPage="' + (currentPage + 1) + '" onclick=\"goPage(' + (currentPage + 1) + ',' + PageSize + ')\"> <input type="text" value="下一页>" class="input" readonly/></a> ';
        tempStr += ' <a href=\"#\" selectPage="' + PageCount + '" onclick=\"goPage(' + (PageCount) + ',' + PageSize + ')\"><input type="text" value="尾页"  class="input" readonly/></a> ';
    } else {
        tempStr += ' <input type="text" value="下一页>"  class="input" readonly/> <input type="text" value="尾页"  class="input" readonly/> ';
    }
    document.getElementById("showbarcon").innerHTML = tempStr1;
    document.getElementById("barcon").innerHTML = tempStr;
    //显示默认页（第一页）
    $('#advertiseList').empty().append(head);
    for (i = (currentPage - 1) * PageSize; i < PageSize * currentPage; i++) {
        $('#advertiseList').append(pageData[i]);
    }
    $('#advertiseList').append(end);
    //显示选择页的内容
    $('a').click(function () {
        var selectPage = $(this).attr('selectPage');
        console.log(selectPage);
        $('#advertiseList').html('');
        $('#advertiseList').append(head);
        for (i = (selectPage - 1) * PageSize; i < PageSize * selectPage; i++) {
            $('#advertiseList').append(pageData[i]);
        }
        $('#advertiseList').append(end);
    });
}
//gaodawei
//showImage
function showEnlargeImg(obj) {
    $(".triggerPicShowEnlarge").each(function () {
        $(this).powerFloat({
            targetMode: "ajax"
        })
    });
    $(obj).powerFloat({
        targetMode: "ajax"
    });
}
