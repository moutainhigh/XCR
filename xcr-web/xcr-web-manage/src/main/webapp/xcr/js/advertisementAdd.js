$(function () {
    var btn = $("#Button3");
    btn.uploadFile({
        url: "/xcr/fileupload/single.htm",
        fileSuffixs: ["jpg", "png", "bmp"],
        buttonFeature: false,
        errorText: "{0}",
        onCheckUpload: function (text) { // 上传时检查文件后缀名不包含在fileSuffixs属性中时触发的回调函数，(text为错误提示文本)
            var str = this.fileSuffixs.join(",.");
//            $("#imgFileError").modal("toggle");
            perviewImage.isCloseImg(true);
            $("#fileList").empty();
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
                $("#imgFileError").modal("toggle");
            } else {
                var srcUrl = msg.substring(msg.indexOf(':') + 1);
                console.log("srcUrl:" + srcUrl);
                if (srcUrl) {
                    perviewImage.isCloseImg(true);
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
        perviewImgStyle: {width: '300px', height: '225px', border: '1px solid #ebebeb'}//设置预览图片的样式
    });

    var upload = btn.data("uploadFileData");
});


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
        if (length >= 60) {
            var value = length = $("#description").val().substring(0, 60);
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
    //for(key in input){
    //	alert("key:"+key+",input[key]:"+input[key]);
    //}
    if ("undefined" != typeof input) {
        for (key in input) {
            console.log(key + "   " + input[key]);
            if (key == "missionType") {
                $("#" + key).val(input[key]);
                $("#" + key).attr("disabled", "disabled");
            } else if (key == "templateCode") {
                if (input["templateCode"] != null) {
                    var htmlvar = "<option value='" + input['templateCode'] + "'>" + input["missionName"] + "</option>";
                    $("#missionTemplate").html(htmlvar);
                    $("#templateCode").val(input["templateCode"]);
                    $("#templateName").val(input["templateName"]);
                    $("#missionTemplate").attr("disabled", "disabled");
                }
            } else if (key == "iconUrl") {
                //$('.uploadImg_picker').hide();
                $("#missionAdd .icon").html('<img id="icon" class="preventImg_pre" src="' + input[key] + '" style="width:100px;">');
            } else if (key == "missionAwardType") {
                $("input[name='missionAwardType']").each(function (i) {
                    if ($(this).val() == input[key]) {
                        $(this).attr("checked", true);
                        if (input[key] == 'NONE') {
                            $('#awardCash').hide();
                            $('#awardScore').hide();
                        } else if (input[key] == 'SCORE') {
                            $('#grantSCORE').val(input["awards"][0].grantNum);
                            $('#awardCash').hide();
                            $('#awardScore').show();
                        } else if (input[key] == 'CASH_AND_SCORE') {
                            $('#grantCASH').val(input["awards"][0].grantNum);
                            $('#grantSCORE').val(input["awards"][0].grantNum);
                            $('#awardCash').show();
                            $('#awardScore').show();
                        } else if (input[key] == 'CASH') {
                            $('#awardCash').show();
                            $('#awardScore').hide();
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