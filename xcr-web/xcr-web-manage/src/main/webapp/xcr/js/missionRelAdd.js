function editCallBack() {
    var input = window.customData;//取传过来的值
    if ("undefined" != typeof input) {
        for (key in input) {
            console.log(key+"  "+input[key]);
            if (key == "details") {
                for (var i = 0; i < input[key].length; i++) {
                    console.log(key + "------------------- " + input[key][i].missonInfoId);
                    if (i == 0) {
                        $("#relMissionAllId").val(input["relMissionId"]);
                        $("#relatedMissionIdfirst").val(input[key][0].missonRelatedId);
                        $("#relatedMissionfirst").val(input[key][0].missonInfoId);
                        $("#relatedLevelfirst").val(input[key][0].level);
                    } else {
                        var _html = '<tr> <td class="title">&nbsp;</td> <td style="width: 50%;float: left"> <div class="col-xs-12"> <div class="row"> <div class="col-xs-7"> <div class="row"> <div class="form-group"><input type="hidden" class="relatedMissionId"/> <select name="" class="form-control relMissionName"> ' + $("#relatedMissionfirst").html() + ' </select> </div> </div> </div> <div class="col-xs-3 col-xs-offset-1"> <div class="row"> <div class="form-group"> <select name="" class="form-control relMissionLevel"> <option value="">请选择</option> <option value="1">1级</option><option value="2">2级</option><option value="3">3级</option><option value="4">4级</option><option value="5">5级</option> </select> </div> </div> </div> <div class="col-xs-1" style="padding-top:8px;"> <div class="row"> <div class="plus remove"><i class="glyphicon glyphicon-minus"></i></div> </div> </div> </div> </div> </td> </tr>';
                        $(".task-list-body").append(_html);
                        $(".relMissionName").eq(i).val(input[key][i].missonInfoId);
                        $(".relMissionLevel").eq(i).val(input[key][i].level);
                        $(".relatedMissionId").eq(i).val(input[key][i].level);
                    }
                }
            } else if (key == "status") {
                if (input[key] == "INIT") {
                } else if (input[key] == "PUBLISH") {
                    $("#isPublish").css("display", "none");
                }
            } else {
                $("#" + key).val(input[key]);//显示data中的数据
            }
        }
        input = null;
    }
    window.customData = null;
}

// 新增条件
var _Html;

$(function () {
    //验证输入的标题长度
    $("#relMissionName").keydown(function () {
        var length = $("#relMissionName").val().length;
        if (length >= 13) {
            var value = length = $("#relMissionName").val().substring(0, 16);
            $("#relMissionName").val(value);
        }
    });

    //验证输入的描述
    $("#relMissionDetail").keydown(function () {
        var length = $("#relMissionDetail").val().length;
        console.log("输入字符串的长度为： " + $("#relMissionDetail").val().length);
        if (length >= 60) {
            var value = length = $("#relMissionDetail").val().substring(0, 60);
            $("#relMissionDetail").val(value);
        }
    });
    $.ajax({
        url: "/xcr/mission/getRelMis.htm",
        type: "POST",
        data: "status=INIT&startIndex=0&endIndex=10&isRelated=1",
        success: function (data) {
            var dataObj = $.parseJSON(data);
            var html = '<option value=""></option>';
            $.each(dataObj, function (commentIndex, comment) {
                html += '<option value="' + comment.id + '">' + comment.name + '</option>';
            });
            $("#relatedMissionfirst").html(html);
            editCallBack();
        }
    });

});

//var _Html=$("#relatedMission").html();
addType($(".add"), $(".task-list-body"), '.remove');
function addType(ele, con, del) {
    ele.click(function () {
        html = '<tr> <td class="title">&nbsp;</td> <td style="width: 50%;float: left"> <div class="col-xs-12"> <div class="row"> <div class="col-xs-7"> <div class="row"> <div class="form-group"> <select name="" class="form-control relMissionName"> ' + $("#relatedMissionfirst").html() + ' </select> </div> </div> </div> <div class="col-xs-3 col-xs-offset-1"> <div class="row"> <div class="form-group"> <select name="" class="form-control relMissionLevel"> <option value="">请选择</option> <option value="1">1级</option><option value="2">2级</option><option value="3">3级</option><option value="4">4级</option><option value="5">5级</option> </select> </div> </div> </div> <div class="col-xs-1" style="padding-top:8px;"> <div class="row"> <div class="plus remove"><i class="glyphicon glyphicon-minus"></i></div> </div> </div> </div> </div> </td> </tr>';
        var a = con.append(html);
        a.find(del).on('click', function () {
            $(this).parent().parent().parent().parent().parent().parent().remove();
        });
    });
    con.on('click', del, function () {
        $(this).parent().parent().parent().parent().parent().parent().remove();
    });
}


//从后台获取关联任务
function saveRelMission(handleStyle) {
    console.log("in saveRelMission ... ");
    var relMissionName = $("#relMissionName").val();
    $("#relMissionNameWrongTip").empty();
    if (relMissionName == '' || relMissionName == null) {
        $("#relMissionNameWrongTip").html("关联任务名称名称不能为空");
        return false;
    }

    var relIdArr = [];
    var relLevelArr = [];
    $(".relMissionName").each(function (index, item) {
        relIdArr.push($(this).val());
    });
    $(".relMissionLevel").each(function (index, item) {
        relLevelArr.push($(this).val());
        console.log($(this).find("option:selected").text());//这个就是jquer循环获取的class对象 你可以用来处理你的逻辑
    });

    for (var i = 0; i < relIdArr.length - 1; i++) {
        for (var j = (i + 1); j < relIdArr.length; j++) {
            if (relIdArr[i] == relIdArr[j]) {
                $("#relTitleJudge").modal("toggle");
                return false;
            }
        }
    }

    var missionRelDet = "[";
    for (var i = 0; i < relIdArr.length; i++) {
        if (i == (relIdArr.length - 1)) {
            if (relIdArr[i] == "" || relIdArr[i] == null || relLevelArr[i] == "" || relLevelArr[i] == null) {
                $("#relLevelJudge").modal("toggle");
                return false;
            } else {
                missionRelDet += '{"missonInfoId":' + relIdArr[i] + ',"level":' + relLevelArr[i] + ',"missonRelatedId":"' + $("#relatedMissionIdfirst").val() + '"}]';
            }
        } else {
            if (relIdArr[i] == "" || relIdArr[i] == null || relLevelArr[i] == "" || relLevelArr[i] == null) {
                $("#relLevelJudge").modal("toggle");
                return false;
            } else {
                missionRelDet += '{"missonInfoId":' + relIdArr[i] + ',"level":' + relLevelArr[i] + ',"missonRelatedId":"' + $("#relatedMissionIdfirst").val() + '"},';
            }
        }
    }
    console.log($.parseJSON(missionRelDet));
    var relMissionDetail = $("#relMissionDetail").val();
    var aaa = JSON.stringify({
        "missonRelatedName": relMissionName,
        "missonRelatedDescription": relMissionDetail,
        "details": $.parseJSON(missionRelDet),
    });
    if (relIdArr.length == 1) {
        $("#judgeRelMisNum").modal("toggle");
    } else {
        var publishFlag;
        if (handleStyle == 'save') {
            publishFlag = false;
        } else if (handleStyle == 'publish') {
            publishFlag = true;
        }
        var requestURl = "";
        if ($("#relMissionAllId").val() == "" || $("#relMissionAllId").val() == null) {
            requestURl = "/xcr/mission/createRel.htm";
        } else {
            requestURl = "/xcr/mission/updateRel.htm";
        }
        $.ajax({
            url: requestURl,
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                "id": $("#relMissionAllId").val(),
                "publish": publishFlag,
                "missonRelatedName": relMissionName,
                "missonRelatedDescription": relMissionDetail,
                "details": $.parseJSON(missionRelDet)
            }),
            success: function (data) {
                if (data == 1) {
                    relationMissionBackList();
                } else if (data == 2) {
                    $("#titleJudge").modal("toggle");
                } else {
                    $("#fail").modal("toggle");
                }
            },
            error: function () {
                $("#fail").modal("toggle");
            }
        });
    }
}