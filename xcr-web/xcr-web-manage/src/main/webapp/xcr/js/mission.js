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
// function awardType() {
//     var awardType = $("input[name='missionAwardType']:checked").val();
//     if (awardType == "CASH") {
//         $('#awardCash').show();
//         $('#awardScore').hide();
//     } else if (awardType == "SCORE") {
//         $('#awardCash').hide();
//         $('#awardScore').show();
//     } else if (awardType == "CASH_AND_SCORE") {
//         $('#awardCash').show();
//         $('#awardScore').show();
//     } else if (awardType == "NONE") {
//         $('#awardCash').hide();
//         $('#awardScore').hide();
//     }
// }

// 获取两日期相差天数
function DateDiff(sDate1, sDate2) {  //sDate1和sDate2是yyyy-MM-dd格式
    var aDate, oDate1, oDate2, iDays;
    aDate = sDate1.split("-");
    oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);  //转换为yyyy-MM-dd格式
    aDate = sDate2.split("-");
    oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);
    iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24); //把相差的毫秒数转换为天数
    return iDays;  //返回相差天数
}

function saveMission(pubStatus) {
    $("#saveTask").attr("disabled", true);
    $("#surePub").attr("disabled", true);
    //用于数据校验，判断积分与现金是否是正数
    var reg = /^\d+(?=\.{0,1}\d+$|$)/;
    //金额校验任意正整数，正小数（小数位不超过2位）
    var isNum = /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;

    //获取数据
    var missionId = $("#missionId").val();
    var missionType = $("#missionType option:selected").val();
    var templateCode = null;
    var continueDate = $("#continueDate").val(); //连续签到时间
    var facePayAmount = $("#facePayAmount").val(); //当面付金额
    var facePayNumber = $("#facePayNumber").val(); //当面付笔数
    var buyAmount = $("#buyAmount").val(); //采购金额

    if ($("#templateCode").val() != null && $("#templateCode").val() != '') {
        templateCode = $("#templateCode").val();
    } else if (missionType == "RECOMMEND" || missionType == "DAILY" || missionType == "STUDY" || missionType == "AUTO_COMPLETE") {
        templateCode = $("#missionTemplate").val();
    }
    var name = $("#missionName").val();
    try {
        $(".preventImg_pre")[0]["src"];
    } catch (err) {
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        $("#imgTag").modal("toggle");
        return false;
    }
    var iconUrl = $(".preventImg_pre")[0]["src"];
    console.log("iconUrl:" + iconUrl);
    console.log("iconUrl.indexOf('http'):" + iconUrl.indexOf("http"));
    //图片做头校验
    if (iconUrl == null || iconUrl.indexOf("http") == -1) {
        $("#picUrlWrongTip").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }

    var awardType = $("#scoreid").val(); //奖励类型
    var grantCASH = $("#grantCASH").val();   //现金数量
    var grantSCORE = $("#grantSCORE").val(); //积分数量
    var taskIsRel = $("#isRelated").val();   //普通任务还是关联任务
    var description = $("#description").val(); //任务秒速

    var durationTimeStart = $("#durationTimeStart").val(); //任务持续时间
    var durationTimeEnd = $("#durationTimeEnd").val();

    var validTimeStart = $("#validTimeStart").val(); //任务有效期
    var validTimeEnd = $("#validTimeEnd").val();

    var soldTicketNum = $("#soldTicketNum").val();
    var discontType = $("#discontType option:selected").val(); //是否奖励
    var courseId = null;

    //日当面付笔数设置默认时间为当天
    //任务有效期为永久
    console.log("templateCode:" + templateCode);
    if (templateCode == 'T009') {
        validTimeStart = new Date().Format("yyyy-MM-dd") + " 00:00:00";
        validTimeEnd = new Date().Format("yyyy-MM-dd") + " 23:59:59";
        durationTimeStart = new Date().Format("yyyy-MM-dd") + " 00:00:00";
        durationTimeEnd = "3000-01-01 00:00:00";
    }
    console.log("validTimeStart:" + validTimeStart);
    console.log("validTimeEnd:" + validTimeEnd);
    console.log("durationTimeStart:" + durationTimeStart);
    console.log("durationTimeEnd:" + durationTimeEnd);

    if ($("#courseId").val() != null && $("#courseId").val() != "") {
        courseId = $("#courseId").val();
    } else if (missionType == "STUDY") {
        courseId = $("#missionTemplate").val();
    }
    var jsonVal = packData(pubStatus, missionId, templateCode, name, iconUrl, awardType, grantCASH, grantSCORE, taskIsRel, missionType, description, courseId, durationTimeStart, durationTimeEnd, validTimeStart, validTimeEnd, soldTicketNum, discontType, continueDate, facePayAmount, facePayNumber, buyAmount);

    if (missionType == null || missionType == "") {
        $("#missionTypeWrongTip").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }
    if (templateCode == null || templateCode == "") {
        $("#missionTemplateWrongTip").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }
    if (name == null || name == "") {
        $("#missionNameWrongTip").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }

    if ($("#missionTemplate").val() == "T007") {
        if (continueDate == null || continueDate == "") {
            $("#continueDateNotNull").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
        if (!reg.test(continueDate)) {
            $("#continueDateinputError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
    }
    if ($("#missionTemplate").val() == "T008") {
        if(taskIsRel == "complex"){
            $("#facePayAmountIsComplex").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
        if (facePayAmount == null || facePayAmount == "") {
            $("#facePayAmountError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
        if (!isNum.test(facePayAmount)) {
            $("#facePayAmountError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
    }
    if ($("#missionTemplate").val() == "T009") {
        if(taskIsRel == "complex"){
            $("#facePayNumberIsComplex").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
        if (facePayNumber == null || facePayNumber == "") {
            $("#facePayNumberError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
        if (!reg.test(facePayNumber)) {
            $("#facePayNumberError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
    }
    if ($("#missionTemplate").val() == "T010") {
        if(taskIsRel == "complex"){
            $("#buyAmountIsComplex").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
        if (buyAmount == null || buyAmount == "") {
            $("#buyAmountError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
        if (!isNum.test(buyAmount)) {
            $("#buyAmountError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
    }


    if (grantSCORE == null || grantSCORE == "") {
        $("#scoreWrongTip").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }

    if (taskIsRel == null || taskIsRel == "") {
        $("#isRelatedWrongTip").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }
    console.log("description:" + description);
    var des = $.trim(description);
    if (des == null || des == "") {
        $("#descriptionNotNullError").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }

    if (durationTimeStart == null || durationTimeStart == "" || durationTimeEnd == null || durationTimeEnd == "") {
        $("#timeError").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }
    if (validTimeStart == null || validTimeStart == "" || validTimeEnd == null || validTimeEnd == "") {
        $("#timeError").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }

    if (durationTimeStart != null && durationTimeStart != "" && durationTimeEnd != null && durationTimeEnd != "") {
        if (durationTimeStart > durationTimeEnd) {
            $("#timeError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
    }
    if (validTimeStart != null && validTimeStart != "" && validTimeEnd != null && validTimeEnd != "") {
        if (validTimeStart > validTimeEnd) {
            $("#timeError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
    }
    var d = new Date();
    var str = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
    var strDate = new Date(str);
    var durationTimeStartDate = new Date(durationTimeStart);
    var validTimeStartDate = new Date(validTimeStart);

    var days = DateDiff(validTimeStart, validTimeEnd) + 1;
    console.log("validTimeStart:" + validTimeStart + "   validTimeEnd:" + validTimeEnd);
    console.log("continueDate:" + continueDate + "   days:" + days);
    if ($("#missionTemplate").val() == "T007") {
        if (continueDate > days) {
            $("#continueDateError").modal("toggle");
            $("#saveTask").attr("disabled", false);
            $("#surePub").attr("disabled", false);
            return false;
        }
    }

    if (templateCode != "T009") {
        if (validTimeStart != null && validTimeStart != "") {
            if (validTimeStartDate.getTime() < strDate.getTime()) {
                $("#createTimeError").modal("toggle");
                $("#saveTask").attr("disabled", false);
                $("#surePub").attr("disabled", false);
                return false;
            }
        }
        if (durationTimeStart != null && durationTimeStart != "") {
            if (durationTimeStartDate.getTime() < strDate.getTime()) {
                $("#createTimeError").modal("toggle");
                $("#saveTask").attr("disabled", false);
                $("#surePub").attr("disabled", false);
                return false;
            }
        }
        if (durationTimeStart != null && durationTimeEnd != null && validTimeStart != null && validTimeEnd != null) {
            if (templateCode == "T008" || templateCode == "T010") {
                if (validTimeStart < durationTimeStart || validTimeEnd >= durationTimeEnd) {
                    $("#validTimeError_2").modal("toggle");
                    $("#saveTask").attr("disabled", false);
                    $("#surePub").attr("disabled", false);
                    return false;
                }
            } else {
                if (validTimeStart < durationTimeStart || validTimeEnd > durationTimeEnd) {
                    $("#validTimeError").modal("toggle");
                    $("#saveTask").attr("disabled", false);
                    $("#surePub").attr("disabled", false);
                    return false;
                }
            }

        }
    }

    if (discontType == null || discontType == "") {
        $("#discontTypeError").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
        return false;
    }

    if (awardType == "NONE" || reg.test(grantCASH) || reg.test(grantSCORE)) {
        var requestURl = "";
        if (missionId == "" || missionId == null) {
            requestURl = "/xcr/mission/create.htm";
        } else {
            requestURl = "/xcr/mission/update.htm";
        }
        console.log("jsonVal:" + JSON.stringify(jsonVal));
        $.ajax({
            url: requestURl,
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(jsonVal),
            success: function (data) {
                $("#saveTask").attr("disabled", false);
                $("#surePub").attr("disabled", false);
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
                $("#saveTask").attr("disabled", false);
                $("#surePub").attr("disabled", false);
            }
        });
    } else {
        $("#dataJudge").modal("toggle");
        $("#saveTask").attr("disabled", false);
        $("#surePub").attr("disabled", false);
    }

}
function packData(pubStatus, missionId, templateCode, name, iconUrl, awardType, grantCASH, grantSCORE, taskIsRel, missionType, description, courseId, durationTimeStart, durationTimeEnd, validTimeStart, validTimeEnd, soldTicketNum, discontType, continueDate, facePayAmount, facePayNumber, buyAmount) {

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
//facePayAmount,facePayNumber,buyAmount
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
            "durationTimeStart": durationTimeStart,
            "durationTimeEnd": durationTimeEnd,
            "validTimeStart": validTimeStart,
            "validTimeEnd": validTimeEnd,
            "soldTicketNum": soldTicketNum,
            "discontType": discontType,
            "link": null,
            "continueDate": continueDate,
            "facePayAmount": facePayAmount,
            "facePayNumber": facePayNumber,
            "buyAmount": buyAmount,
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
            "durationTimeStart": durationTimeStart,
            "durationTimeEnd": durationTimeEnd,
            "validTimeStart": validTimeStart,
            "validTimeEnd": validTimeEnd,
            "soldTicketNum": soldTicketNum,
            "discontType": discontType,
            "link": null,
            "continueDate": continueDate,
            "facePayAmount": facePayAmount,
            "facePayNumber": facePayNumber,
            "buyAmount": buyAmount,
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
            "durationTimeStart": durationTimeStart,
            "durationTimeEnd": durationTimeEnd,
            "validTimeStart": validTimeStart,
            "validTimeEnd": validTimeEnd,
            "soldTicketNum": soldTicketNum,
            "discontType": discontType,
            "link": null,
            "continueDate": continueDate,
            "facePayAmount": facePayAmount,
            "facePayNumber": facePayNumber,
            "buyAmount": buyAmount,
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
            "attachments": null
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
            "durationTimeStart": durationTimeStart,
            "durationTimeEnd": durationTimeEnd,
            "validTimeStart": validTimeStart,
            "validTimeEnd": validTimeEnd,
            "soldTicketNum": soldTicketNum,
            "discontType": discontType,
            "link": null,
            "continueDate": continueDate,
            "facePayAmount": facePayAmount,
            "facePayNumber": facePayNumber,
            "buyAmount": buyAmount,
            "awards": null,
            "attachments": null
        };
    }
    return data;
}

Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}