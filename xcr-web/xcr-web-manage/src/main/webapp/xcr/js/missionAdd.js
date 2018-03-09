$(function () {
    var btn = $("#Button1");
    btn.uploadFile({
        url: "/xcr/fileupload/single.htm",
        fileSuffixs: ["jpg", "png", "bmp"],
        buttonFeature: false,
        errorText: "{0}",
        onCheckUpload: function (text) { // 上传时检查文件后缀名不包含在fileSuffixs属性中时触发的回调函数，(text为错误提示文本)
            if (text != null && text != "") {
                var str = this.fileSuffixs.join(",.");
                $("#imgFileError").modal("toggle");
                perviewImage.isCloseImg(true);
                $("#fileList").empty();
            }
            return false;
        },
        onSubmitHandle: function (uploadFileNumber) { //提交上传时的回调函数，uploadFileNumber为当前上传的文件数量
            return true;
        },
        onSameFilesHandle: function (file) { //当重复选择相同的文件时触发
            return false;
        },
        maximumFilesUpload: 1,//最大文件上传数
        onComplete: function (msg) {
            console.log(msg);
            if (msg == "error") {
                $("#fileList").empty();
            } else {
                var srcUrl = msg.substring(msg.indexOf(':') + 1);
                if (srcUrl) {
                    perviewImage.isCloseImg(true);
                    $('img#icon.preventImg_pre').remove();
                    $('img.preventImg_pre').attr('src', srcUrl);
                } else {
                    alert("上传失败!请重新上传");
                }
            }
        },
        onChosen: function (file, obj) {
            console.log("111111111111");
            $("#fileList").empty();
            $("#missionAdd .icon").empty();

            //判断图片大小
            var imgSize;
            if (obj.files) {
                imgSize = obj.files[0].size;
            } else {
                var filePath = file;
                var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
                var file = fileSystem.GetFile(filePath);
                imgSize = file.Size;
            }
            if (imgSize > 2097152) {
                $("#imgdataJudge2").modal("toggle");
                $("#fileList").empty();
                obj.outerHTML = '';
                return false;
            }

            return true;
        },
        perviewImageElementId: "fileList", //设置预览图片的元素id
        perviewImgStyle: {width: '300px', height: '200px', border: '1px solid #ebebeb'}//设置预览图片的样式
    });

    var upload = btn.data("uploadFileData");
});


a0show();
$(document).ready(function () {
    $("#missionTemplate").change(function () {
        a0show();
    });
})
function a0show() {
    if ($("#missionTemplate").val() == 585) {//这对应你需要选择的下拉值
        $(".ticketClass").css("display", "table-row");
    } else {
        $(".ticketClass").css("display", "none");
    }
}

discontshow();
$(document).ready(function () {
    $("#discontType").change(function () {
        discontshow();
    });
})
function discontshow() {
    if ($("#discontType").val() == "DISCONTOK") {//这对应你需要选择的下拉值
        $(".discontClass").css("display", "table-row");
    } else {
        $(".discontClass").css("display", "none");
    }
}

//根据任务显示配置项
$(document).ready(function () {
    $("#missionTemplate").change(function () {
        if ($("#missionTemplate").val() == "T007") {
            $(".continuouSignClass").css("display", "table-row");
        } else {
            $(".continuouSignClass").css("display", "none");
        }
        if ($("#missionTemplate").val() == "T008") {
            $(".facePayAmountClass").css("display", "table-row");
        } else {
            $(".facePayAmountClass").css("display", "none");
        }
        if ($("#missionTemplate").val() == "T009") {
            $(".facePayNumberClass").css("display", "table-row");
            $(".statisticsTime").css("display", "none");
            $(".validTimeClass").css("display", "none");
        } else {
            $(".statisticsTime").css("display", "table-row");
            $(".validTimeClass").css("display", "table-row");
            $(".facePayNumberClass").css("display", "none");
        }
        if ($("#missionTemplate").val() == "T010") {
            $(".buyAmountClass").css("display", "table-row");
        } else {
            $(".buyAmountClass").css("display", "none");
        }
    })
})


$(function () {
    $(".demoform").Validform({
        tiptype: function (msg, o, cssctl) {
            if (!o.obj.is("form")) {
                var objtip = o.obj.siblings(".Validform_checktip");
                cssctl(objtip, o.type);
                objtip.text(msg);
            } else {
                var objtip = o.obj.find("#msgdemo");
                cssctl(objtip, o.type);
                objtip.text(msg);
            }
        }
    });

    //验证输入的标题长度
    $("#missionName").keydown(function () {
        var length = $("#missionName").val().length;
        console.log("输入字符串的长度为： " + $("#missionName").val().length);
        if (length >= 20) {
            var value = length = $("#missionName").val().substring(0, 20);
            $("#missionName").val(value);
        }
    });
    //验证输入的现金
    $("#grantCASH").keydown(function () {
        var length = $("#grantCASH").val().length;
        console.log("输入字符串的长度为： " + $("#grantCASH").val().length);
        if (length >= 4) {
            var value = length = $("#grantCASH").val().substring(0, 3);
            $("#grantCASH").val(value);
        }
    });
    //验证输入的积分
    $("#grantSCORE").keydown(function () {
        var length = $("#grantSCORE").val().length;
        console.log("输入字符串的长度为： " + $("#grantSCORE").val().length);
        if (length >= 4) {
            var value = length = $("#grantSCORE").val().substring(0, 3);
            $("#grantSCORE").val(value);
        }
    });
    //验证输入的描述
    $("#description").keydown(function () {
        var length = $("#description").val().length;
        console.log("输入字符串的长度为： " + $("#description").val().length);
        if (length >= 600) {
            var value = length = $("#description").val().substring(0, 600);
            $("#description").val(value);
        }
    });
    $("#missionType").change(function () {
        if ($(this).val() == "STUDY") {
            $("#isRelated").val("simple");
            $("#isRelated").change();
            $("#isRelated").attr("disabled", "true");
        } else {
            $("#isRelated").removeAttr("disabled");
            $("#isRelated").val("");
            $("#isRelated").change();
        }
    });

    //编辑是用于显示数据
    var input = window.customData;//取传过来的值
    var awardType = $("input[name='missionAwardType']");
    if ("undefined" != typeof input) {
        for (key in input) {
            console.log(key + "   " + input[key]);
            if (key == "continueDate") {
                console.log(key + "   " + input[key]);
                var day = input[key];
                if (day != null && day != 0) {
                    $('.continuouSignClass').show();
                    $("#continueDate").val(input["continueDate"]);//显示data中的数据
                } else {
                    $('.continuouSignClass').hide();
                }
            }
            if (key == "facePayNumber") {
                console.log(key + "   " + input[key]);
                var day = input[key];
                if (day != null && day != 0) {
                    $('.facePayNumberClass').show();
                    $("#facePayNumber").val(input["facePayNumber"]);//显示data中的数据
                    $('.statisticsTime').hide();
                    $('.validTimeClass').hide();
                } else {
                    $('.facePayNumberClass').hide();
                }
            }
            if (key == "facePayAmount") {
                console.log(key + "   " + input[key]);
                var day = input[key];
                if (day != null && day != 0) {
                    $('.facePayAmountClass').show();
                    $("#facePayAmount").val(input["facePayAmount"]);//显示data中的数据
                } else {
                    $('.facePayAmountClass').hide();
                }
            }
            if (key == "buyAmount") {
                console.log(key + "   " + input[key]);
                var day = input[key];
                if (day != null && day != 0) {
                    $('.buyAmountClass').show();
                    $("#buyAmount").val(input["buyAmount"]);//显示data中的数据
                } else {
                    $('.buyAmountClass').hide();
                }
            }

            if (key == "missionType") {
                $("#" + key).val(input[key]);
                $("#" + key).attr("disabled", "disabled");
            } else if (key == "templateCode") {
                if (input["templateCode"] != null) {
                    var htmlvar = "<option value='" + input['templateCode'] + "'>" + input["templateName"] + "</option>";
                    $("#missionTemplate").html(htmlvar);
                    $("#templateCode").val(input["templateCode"]);
                    $("#templateName").val(input["templateName"]);
                    $("#missionTemplate").attr("disabled", "disabled");
                }
            } else if (key == "iconUrl") {
                //$('.uploadImg_picker').hide();
                $("#missionAdd .icon").html('<img id="icon" class="preventImg_pre" src="' + input[key] + '" style="width:300px;height: 150px;margin-top: 10px;"/>');
            } else if (key == "missionAwardType") {
                $("input[name='missionAwardType']").each(function (i) {
                    if ($(this).val() == input[key]) {
                        if (input[key] == 'SCORE') {
                            console.log("积分奖励：" + key + "   " + input[key]);
                            console.log("积分奖励:" + key + "   " + input["awards"][0].grantNum);
                            $('#grantSCORE').val(input["awards"][0].grantNum);
                        }
                    }
                });
            } else if (key == "isRelated") {
                if (true == input[key]) {
                    $("#" + key).val("complex");
                    $("#surePub").attr("disabled", true);
                } else if (false == input[key]) {
                    if (input["missionType"] == "STUDY") {
                        $("#" + key).val("simple");
                        $("#isRelated").attr("disabled", "disabled");
                    } else {
                        $("#" + key).val("simple");
                    }
                }
            } else {
                $("#" + key).val(input[key]);//显示data中的数据
            }
        }
    }
    input = null;
});