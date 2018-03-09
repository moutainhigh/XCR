//跳转到任务主页（普通任务列表）
function normalMissionList() {
    javascript:loadHtml('/xcr/mission.htm');
}
//跳转到关联任务列表
function relationMissionList() {
    javascript:loadHtml('/xcr/relationMissionList.htm');
}

function relationMissionBackList() {
    javascript:loadHtml('/xcr/relationMissionBackList.htm');
}

function getTemplate(value) {
    $("#missionTemplate").attr("disabled", "disabled");
    $("#missionTemplate").html("");
    var missionType = $("#missionType").val();
    $.ajax({
        url: "/xcr/mission/queryByMisType.htm",
        type: "POST",
        dataType: "json",
        data: {"missionType": missionType},
        success: function (data) {
            var html = '<option value="">-请选择-</option>';
            $.each(data, function (commentIndex, comment) {
                if (missionType == "RECOMMEND" || missionType == "DAILY") {
                    html += '<option value="' + comment['templateCode'] + '">' + comment['name'] + '</option>';
                }
                if (missionType == "STUDY") {
                    html += '<option value="' + comment['id'] + '">' + comment['name'] + '</option>';
                }
            });
            $("#missionTemplate").html(html);
            $("#missionTemplate").removeAttr("disabled");
            $("#missionName").val("");
            $("#missionName").attr("disabled", "disabled");
        }
    });
}
//选择魔板是默认显示魔板的名称为任务或者课堂的名称
function changeName() {
    var str = $("#missionType").val();
    if (str == "" || str == "-请选择-") {
        var html = '<option value="">-请选择-</option>';
        $("#missionTemplate").html(html);
        return;
    }
    var name = $("#missionTemplate").find("option:selected").text();
    console.log("missionName=" + name);
    if (name == "-请选择-") {
        $("#missionName").val("");
        return;
    } else {
        $("#missionName").val(name);
        if (str == 'STUDY') {
        } else {
            $("#missionName").removeAttr("disabled");
        }
    }
}

//判断是否是关联任务，是的话确认发布按钮不能点击
function taskIsRel() {
    var value = $("#isRelated").val();
    if (value == "complex") {
        $("#surePub").attr("disabled", true);
    } else if (value == "simple") {
        $("#surePub").attr("disabled", false);
    }
}

//判断奖励类型
function awardType() {
    var awardType = $("input[name='missionAwardType']:checked").val();
    if (awardType == "CASH") {
        $('#awardCash').show();
        $('#awardScore').hide();
    } else if (awardType == "SCORE") {
        $('#awardCash').hide();
        $('#awardScore').show();
    } else if (awardType == "CASH_AND_SCORE") {
        $('#awardCash').show();
        $('#awardScore').show();
    } else if (awardType == "NONE") {
        $('#awardCash').hide();
        $('#awardScore').hide();
    }
}
function saveMission(pubStatus) {
    //用于数据校验，判断积分与现金是否是正数
    var reg = /^\d+(?=\.{0,1}\d+$|$)/;
    //获取数据
    var missionId = $("#missionId").val();
    var missionType = $("#missionType option:selected").val();
    var templateCode = null;
    if ($("#templateCode").val() != null && $("#templateCode").val() != '') {
        templateCode = $("#templateCode").val();
    } else if (missionType == "RECOMMEND" || missionType == "DAILY" || missionType == "STUDY") {
        templateCode = $("#missionTemplate").val();
    }
    var name = $("#missionName").val();
    try {
        $(".preventImg_pre")[0]["src"];
    } catch (err) {
        $("#imgTag").modal("toggle");
        return false;
    }
    var iconUrl = $(".preventImg_pre")[0]["src"];
    var awardType = $('input:radio[name="missionAwardType"]:checked').val();
    var grantCASH = $("#grantCASH").val();
    var grantSCORE = $("#grantSCORE").val();
    var taskIsRel = $("#isRelated").val();
    var description = $("#description").val();
    var date1=$("#date1").val();
    var date2=$("#date2").val();
    var soldTicketNum=$("#soldTicketNum").val();
    var lastingTime=$("#lastingTime").val();
    var discontType = $("#discontType option:selected").val();
    var courseId = null;
    if ($("#courseId").val() != null && $("#courseId").val() != "") {
        courseId = $("#courseId").val();
    } else if (missionType == "STUDY") {
        courseId = $("#missionTemplate").val();
    }
    var jsonVal = packData(pubStatus, missionId, templateCode, name, iconUrl, awardType, grantCASH, grantSCORE, taskIsRel, missionType, description, courseId,date1,date2,lastingTime,soldTicketNum,discontType);
    console.log(templateCode);
    $("#missionTypeWrongTip").empty();
    $("#missionTemplateWrongTip").empty();
    $("#missionNameWrongTip").empty();
    $("#awardTypeWrongTip").empty();
    $("#isRelatedWrongTip").empty();
    if (missionType == null || missionType == "") {
        $("#missionTypeWrongTip").html("任务分类不能为空");
        return false;
    } else if (templateCode == null || templateCode == "") {
        $("#missionTemplateWrongTip").html("任务/课堂不能为空");
        return false;
    } else if (name == null || name == "") {
        $("#missionNameWrongTip").html("任务名称不能为空");
        return false;
    } else if (awardType == null || awardType == "") {
        $("#awardTypeWrongTip").html("奖励类型不能为空");
        return false;
    } else if (taskIsRel == null || taskIsRel == "") {
        $("#isRelatedWrongTip").html("任务类型不能为空");
        return false;
    } else if (discontType == null || discontType == "") {
        $("#discontType").html("奖励类型不能为空");
        return false;
    }else {
        if (awardType == "NONE" || reg.test(grantCASH) || reg.test(grantSCORE)) {
            var requestURl = "";
            if (missionId == "" || missionId == null) {
                requestURl = "/xcr/mission/create.htm";
            } else {
                requestURl = "/xcr/mission/update.htm";
            }
            $.ajax({
                url: requestURl,
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify(jsonVal),
                success: function (data) {
                    if (data.flag == true) {
                        $("#success").modal("toggle");
                        $('#success').on('hidden.bs.modal', function () {
                            //跳转到任务列表
                            normalMissionList();
                        })

                    } else if (data.flag == false) {
                        //$("#titleJudge").modal("toggle");
                        alert(data.errorMessage);
                    } else {
                    }
                },
                error: function (data) {
                    $("#serverFail").modal("toggle");
                }
            });
        } else {
            $("#dataJudge").modal("toggle");
        }
    }
}
function packData(pubStatus, missionId, templateCode, name, iconUrl, awardType, grantCASH, grantSCORE, taskIsRel, missionType, description, courseId,date1,date2,lastingTime,soldTicketNum,discontType) {

    var related;
    var publish;
    if (taskIsRel == "simple") {
        related = false;
    } else if (taskIsRel == "complex") {
        related = true;
    }
    if (pubStatus == "INIT") {
        publish = false;
    } else if (pubStatus == "PUBLISH") {
        publish = true;
    }
    var data = null;
    if (awardType == "CASH") {
        data = {
            "id": missionId,
            "type": missionType,
            "name": name,
            "iconUrl": iconUrl,
            "awardType": awardType,
            "templateCode": templateCode,
            "description": description,
            "related": related,
            "publish": publish,
            "courseId": courseId,
            "dateStart":date1,
            "dateEnd":date2,
            "lastingTime":lastingTime,
            "soldTicketNum":soldTicketNum,
            "discontType":discontType,
            "link": null,
            "awards": [{
                "awardType": awardType,
                "grantNum": grantCASH,
                "grantStyle": null
            }],
            "attachments": null
        };
    } else if (awardType == "SCORE") {
        data = {
            "id": missionId,
            "type": missionType,
            "name": name,
            "iconUrl": iconUrl,
            "awardType": awardType,
            "courseId": courseId,
            "templateCode": templateCode,
            "description": description,
            "related": related,
            "publish": publish,
            "dateStart":date1,
            "dateEnd":date2,
            "lastingTime":lastingTime,
            "soldTicketNum":soldTicketNum,
            "discontType":discontType,
            "link": null,
            "awards": [{
                "awardType": awardType,
                "grantStyle": "",
                "grantNum": grantSCORE
            }],
            "attachments": null
        };
    } else if (awardType == "CASH_AND_SCORE") {
        data = {
            "id": missionId,
            "type": missionType,
            "name": name,
            "iconUrl": iconUrl,
            "awardType": awardType,
            "templateCode": templateCode,
            "courseId": courseId,
            "description": description,
            "related": related,
            "publish": publish,
            "dateStart":date1,
            "dateEnd":date2,
            "lastingTime":lastingTime,
            "soldTicketNum":soldTicketNum,
            "discontType":discontType,
            "link": null,
            "awards": [{
                "awardType": awardType,
                "grantStyle": "",
                "grantNum": grantCASH
            },
                {
                    "awardType": awardType,
                    "grantStyle": "",
                    "grantNum": grantSCORE
                }],
            "attachments": null,
        };
    } else if (awardType == "NONE") {
        data = {
            "id": missionId,
            "type": missionType,
            "name": name,
            "iconUrl": iconUrl,
            "awardType": awardType,
            "templateCode": templateCode,
            "courseId": courseId,
            "description": description,
            "related": related,
            "publish": publish,
            "dateStart":date1,
            "dateEnd":date2,
            "lastingTime":lastingTime,
            "soldTicketNum":soldTicketNum,
            "discontType":discontType,
            "link": null,
            "awards": null,
            "attachments": null,
        };
    }
    return data;
}