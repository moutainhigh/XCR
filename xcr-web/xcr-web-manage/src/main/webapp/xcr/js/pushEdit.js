//跳转到任务主页（普通任务列表）
function msgList() {
    javascript:loadHtml('/xcr/msg.htm');
}
$(function () {
    console.log("Button1 .... ");
    var btn = $("#Button1");
    btn.uploadFile({
        url: "/xcr/fileupload/single.htm",
        fileSuffixs: ["jpg", "png", "bmp"],
        buttonFeature: false,
        errorText: "{0}",
        onCheckUpload: function (text) { // 上传时检查文件后缀名不包含在fileSuffixs属性中时触发的回调函数，(text为错误提示文本)
            if (text != null && text != "") {
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
                    $("#uploadError").modal("toggle");
                }
            }
        },
        onChosen: function (file, obj) {
            console.log("111111111111");
            $("#fileList").empty();
            $("#msgEdit .icon").empty();

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

$(function () {
    //验证输入的标题长度
    $("#title1").keydown(function () {
        $("#wrongTip").empty();
        $("#showURLWrongTip").empty();
        var length = $("#title").val().length;
        console.log(length);
        if (length >= 15) {
            var value = length = $("#title").val().substring(0, 16);
            $("#title").val(value);
            // $("#wrongTip").html("字符数量达到最大，要求十六字符内");
        }
        var oEvent = window.event;
        if (oEvent.keyCode == 8) {
            $("#title").val("");
        }
    });
    //验证输入的链接
    $("#msgUrl").blur(function () {
        var sear = new RegExp('http://');
        var sears = new RegExp('https://');
        var pattern_chin = /[\u4e00-\u9fa5]/g;

        $("#showURLWrongTip").empty();
        var msgUrl = $("#msgUrl").val();
        var length = $("#msgUrl").val().length;
        console.log(length);
        if (length >= 6) {
            if (msgUrl.match(pattern_chin) == null) {
                if (sear.test(msgUrl) || sears.test(msgUrl)) {
                } else {
                    $("#showURLWrongTip").empty();
                    // $("#showURLWrongTip").html("应以http://或者https://开头");
                }
            } else {
                $("#showURLWrongTip").empty();
                // $("#showURLWrongTip").html("链接当中不能含有中文");
            }
        }
    });
    $("#title").blur(function () {
        $("#wrongTip").empty();
        $("#showURLWrongTip").empty();
        var length = $("#title").val().length;
        console.log("长度是：" + length);
        if (length > 16) {
            var value = length = $("#title").val().substring(0, 16);
            $("#title").val(value);
            // $("#wrongTip").html("字符数量达到最大，要求十六字符内");
        }
    });

    var input = window.customData;//取传过来的值
    if ("undefined" != typeof input) {
        for (key in input) {
            console.log(key + "----------" + input[key]);
            if (key == "imageUrl") {
                $("#msgEdit .icon").html('<img id="icon" class="preventImg_pre" src="' + input[key] + '" style="width:410px;height: 185px;margin-left: 10px;">');
                //$('.uploadImg_picker').hide();
            } else if (key == "pushType") {
                if (input[key] == 1) {//单人推送
                    $("#pushType").find("option[value='1']").attr("selected", true);
                    $("#shopNo").css("display", "table-row");
                    $("#areaPush").css("display", "none");
                }
                if (input[key] == 2) {
                    $("#pushType").find("option[value='2']").attr("selected", true);
                    $("#shopNo").css("display", "none");
                    $("#areaPush").css("display", "table-row");
                    console.log("区域推送----------" + input["shopNo"]);
                    var areaStr = input["areaStr"];
                    var areaCode = areaStr.split(",");
                    console.log("areaCode:" + areaCode);
                    var s = document.getElementsByName("area");
                    for (var i = 0; i < s.length; i++) {
                        for (var j = 0; j < areaCode.length; j++) {
                            if (s[i].value == areaCode[j]) {
                                s[i].checked = true;
                            }
                        }
                    }
                }
            } else if (key == "shopNo") {
                $("#shopId").val(input[key]);
            } else if (key == "contentFrom") {
                if (input[key] == "1") {//消息编辑器
                    $('#summernote').summernote('code', input["contentHtml"]);
                    $("#contentType_html").attr("checked", true);
                    $("#msg_contentHtml").css("display", "table-row");
                    $("#msg_contentUrl").css("display", "none");
                }
            } else {
                $("#" + key).val(input[key]);//显示data中的数据
            }
        }
        input = null;
        $("#msgEditLi").attr("class", "active");
        $("#msgListLi").removeClass("active");
    }
});
$("#messageSave").click(function () {
    console.log("in messageSave ...  ");
    var id = $("#id").val();
    var title = $("#title").val();
    var contentHtml = $('#summernote').summernote('code'); //富文本内容
    try {
        $(".preventImg_pre")[0]["src"];
    } catch (err) {
        $("#imgNotNull").modal("toggle");
        return;
    }
    var imageUrl = $(".preventImg_pre")[0]["src"];
    var msgUrl = $("#msgUrl").val();
    var pushType = $("#pushType").val(); //推送类型 0：所有，1：定向,2:区域
    var shopNo = "";
    if (pushType == "1") {
        shopNo = $("#shopId").val();
    }

    //获取多选框的选中值
    var areaArr = [];
    $("input[type='checkbox']:checked").each(function (index, item) {//
        areaArr.push($(this).val());
    });
    console.log("areaArr:" + areaArr);
    if (pushType == 2) {
        if (areaArr == null || areaArr == "") {
            $("#areaArrNotNull").modal("toggle");
            return;
        }
        // if (areaArr.length > 2) {
        //     $("#areaArrOnlyOne").modal("toggle");
        //     return;
        // }
    }

    var sear = new RegExp('http://');
    var sears = new RegExp('https://');
    var pattern_chin = /[\u4e00-\u9fa5]/g;

    var contentType = $('input:radio:checked').val();
    console.log("contentType:" + contentType);
    if (contentType == 1) {
        if (contentHtml == null || contentHtml == "" || contentHtml == "<p><br></p>") {
            $("#contentHtmlNotNull").modal("toggle");
            return false;
        }
    }
    if (contentType == -1) {
        if (msgUrl == null || msgUrl == "") {
            $("#urlNotNull").modal("toggle");
            return false;
        } else {
            if (!sear.test(msgUrl) && !sears.test(msgUrl)) {
                $("#urlHeadError").modal("toggle");
                return false;
            }
            if (msgUrl.match(pattern_chin) != null) {
                $("#urlSpellError").modal("toggle");
                return false;
            }
        }
    }

    var param = {
        "id": id,
        "title": title,
        "imageUrl": imageUrl,
        "msgUrl": msgUrl,
        "status": "0",
        "type": "0",
        "createUid": 1,
        "createTime": new Date(),
        "modifyUid": 1,
        "modifyTime": new Date(),
        "pushType": pushType,
        "shopNo": shopNo,
        "areaArr": areaArr,
        "contentHtml": contentHtml,
        "contentFrom": contentType
    };
    console.log(param);


    $("#wrongTip").empty();
    $("#showURLWrongTip").empty();
    if (title == null || title == "") {
        $("#titleNotNull").modal("toggle");
        return false;
    }
    if (imageUrl == null || imageUrl == "") {
        $("#imgNotNull").modal("toggle");
        return false;
    }
    $.ajax({
        url: "/xcr/message/saveMessage.htm",
        type: "POST",
        data: JSON.stringify(param),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data == 1) {
                $("#successModel").modal("toggle");
                $('#successModel').on('hidden.bs.modal', function () {
                    msgList();
                })
            } else {
                $("#fail").modal("toggle");
                $('#fail').on('hidden.bs.modal', function () {
                    return false;
                })
            }
        }
    });
});
function _confirmSave() {
    var id = $("#id").val();
    var title = $("#title").val();
    var contentHtml = $('#summernote').summernote('code'); //富文本内容
    try {
        $(".preventImg_pre")[0]["src"];
    } catch (err) {
        $("#imgNotNull").modal("toggle");
        return false;
    }
    var sear = new RegExp('http://');
    var sears = new RegExp('https://');
    var pattern_chin = /[\u4e00-\u9fa5]/g;
    console.log(2222);
    var imageUrl = $(".preventImg_pre")[0]["src"];
    var msgUrl = $("#msgUrl").val();

    var contentType = $('input:radio:checked').val();
    console.log("radioChecked:" + contentType);
    if (contentType == 1) {
        if (contentHtml == null || contentHtml == "" || contentHtml == "<p><br></p>") {
            $("#contentHtmlNotNull").modal("toggle");
            return false;
        }
    }
    if (contentType == -1) {
        if (msgUrl == null || msgUrl == "") {
            $("#urlNotNull").modal("toggle");
            return false;
        } else {
            if (!sear.test(msgUrl) && !sears.test(msgUrl)) {
                $("#urlHeadError").modal("toggle");
                return false;
            }
            if (msgUrl.match(pattern_chin) != null) {
                $("#urlSpellError").modal("toggle");
                return false;
            }
        }
    }
    var pushType = $("#pushType").val(); //推送类型 0：所有，1：定向
    var shopNo = "";
    if (pushType == "1") {
        shopNo = $("#shopId").val();
    }
    //获取多选框的选中值
    var areaArr = [];
    $("input[type='checkbox']:checked").each(function (index, item) {//
        areaArr.push($(this).val());
    });
    console.log("areaArr:" + areaArr);
    if (pushType == 2) {
        if (areaArr == null || areaArr == "") {
            $("#areaArrNotNull").modal("toggle");
            return;
        }
    }
    var param = {
        "id": id,
        "title": title,
        "imageUrl": imageUrl,
        "msgUrl": msgUrl,
        "status": "1",
        "type": "0",
        "createUid": 1,
        "createTime": new Date(),
        "modifyUid": 1,
        "modifyTime": new Date(),
        "releasesTime": new Date(),
        "pushType": pushType,
        "shopNo": shopNo,
        "areaArr": areaArr,
        "contentHtml": contentHtml,
        "contentFrom": contentType
    };
    $("#wrongTip").empty();
    $("#showURLWrongTip").empty();
    if (title == null || title == "") {
        $("#titleNotNull").modal("toggle");
        return;
    }
    if (imageUrl == null || imageUrl == "") {
        $("#imgNotNull").modal("toggle");
        return;
    }
    win.confirm('系统提示', '确定发布吗？发布立即生效>>>', function (r) {
        if (r === true) {
            $.ajax({
                url: "/xcr/message/releaseMessage.htm",
                type: "POST",
                data: JSON.stringify(param),
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    if (data == 1) {
                        $("#successModel").modal("toggle");
                        $('#successModel').on('hidden.bs.modal', function () {
                            msgList();
                        })

                    } else {
                        $("#fail").modal("toggle");
                        $('#fail').on('hidden.bs.modal', function () {
                            return false;
                        })
                    }
                }
            });
        }
    });
}

$(document).ready(function () {
    $("#pushType").change(function () {
        console.log($("#pushType").val());
        if ($("#pushType").val() == "1") {//这对应你需要选择的下拉值
            $("#shopNo").css("display", "table-row");
            $("#areaPush").css("display", "none");
        } else if ($("#pushType").val() == "2") {
            $("#areaPush").css("display", "table-row");
            $("#shopNo").css("display", "none");
        } else {
            $("#shopNo").css("display", "none");
            $("#areaPush").css("display", "none");
        }
    });

    $(":radio").click(function () {
        console.log($(this).val());
        var type = $(this).val();
        if (type == -1) {
            $("#msg_contentUrl").css("display", "table-row");
            $("#msg_contentHtml").css("display", "none");
        } else if (type == 1) {
            $("#msg_contentUrl").css("display", "none");
            $("#msg_contentHtml").css("display", "table-row");
        } else {
            $("#msg_contentUrl").css("display", "table-row");
            $("#msg_contentHtml").css("display", "none");
        }
    });
});