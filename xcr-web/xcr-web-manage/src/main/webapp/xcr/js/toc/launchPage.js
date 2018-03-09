
var queryParams = function (params) {
    var param = {
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
    // var detailUrl = "/xcr/order/" + row.orderNo + "/detail.htm";
    var launchid = row.id;
    var imagePreview = row.imagePreview;
    var currentState = row.currentState;
    var launchAURL = row.launchAURL;
    var url1 = '';
    var url2 = '';
    url1 = '<button  value="' + launchid+"*"+imagePreview+"*"+launchAURL + '" onclick="updateImage(this.value)" style="margin-right:20px;" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>修改</h4></button>';
    if(row.currentState==0){
    	url2 = '<button  value="' + launchid+"*"+1 + '" onclick="updateState(this.value)" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>启用</h4></button>';
    }else{
    	url2 = '<button  value="' + launchid+"*"+0 + '" onclick="updateState(this.value)" class="btn btn-group btn-sm rightSize detailBtn edit"><h4><i class="fa fa-search-plus"></i>停用</h4></button>';
    }

    return [
        url1, url2
    ].join('');
};
var urlFormatter = function (value, row, index) {//赋予的参数
    // var detailUrl = "/xcr/order/" + row.orderNo + "/detail.htm";
    var imagePreview = row.imagePreview;
    var launchid = row.id;
    
    var urlImage2 = '<img style="width:75px;height:50px;" onmouseover="showEnlargeImg(this);" src='+imagePreview+'>'; 
    var urlImage = '<p class="triggerPicShowEnlarge"  rel="' + imagePreview + '" data-id=' + launchid + ' onmouseover="showEnlargeImg(this);" style="width:100%;height:100%;"><img style="width: 60px;height: 40px;" src="' + imagePreview + '"/></p>';
    return [
            urlImage
    ].join('');
};
var stateFormatter = function (value, row, index) {
	var typex = '';
    if (row.currentState == 0) {
        typex = "已停用";
    } else {
        typex = "使用中";
    }
    return ['<h4><i></i>' + typex + '</h4>'
    ].join('');
}
$('#launchPage-table').bootstrapTable({
    url: "/xcr/adList.htm",
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
    pagination: false,                   //是否显示分页（*）
    strictSearch: true,
    showColumns: false,                  //是否显示所有的列
    showRefresh: true,                  //是否显示刷新按钮
    showToggle: true,                    //是否显示详细视图和列表视图
    clickToSelect: true,                //是否启用点击选中行
    minimumCountColumns: 2,          //最少允许的列数 clickToSelect: true, //是否启用点击选中行
    pageNumber: 1,                   //初始化加载第一页，默认第一页
    pageSize: 5,                    //每页的记录行数（*）
    responseHandler: responseHandler,

    columns: columns = [
        {
            field: 'lastModifyTime',
            title: '最后更改时间',
            align: 'center',
            sortable: false,
            valign: 'middle'
        }, {
            field: 'imagePreview',
            title: '图片预览',
            align: 'center',
            sortable: false,
            valign: 'middle',
            formatter: urlFormatter
        }, {
            field: 'launchAURL',
            title: '跳转链接',
            align: 'center',
            sortable: false,
            valign: 'middle',
            width:300
        }, {
            field: 'currentState',
            title: '当前状态',
            align: 'center',
            sortable: false,
            formatter: stateFormatter,
            valign: 'middle'
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: actionFormatter//自定义方法，添加操作按钮
        }],
    onLoadSuccess: function (data) { //加载成功时执行
        console.log(data);
    },
    onLoadError: function (res) { //加载失败时执行
        console.log(res);
    }
});
function updateImage(code) {
	var strs= new Array();
	strs=code.split("*");
	var launchid=strs[0];
	var imagePreview=strs[1];
	var launchAURL = strs[2];
	if(launchAURL==""){
		launchAURL = '';
	}
	$("#myModalTitle").html('修改<input type="hidden" value='+launchid+' id="launchid" name="launchid">');
	$("#myModalContent").html('<div style="padding-top:10px;">跳转链接：<input style="width:250px;" type="text" value="'+launchAURL+'" id="aImageURL" name="aImageURL"></div><br><div style="padding-top:15px;"><span style="color:#f00;">*</span>图片&nbsp;&nbsp;(图片大小不超过1M,建议像素1080*1920)</div><br><div id="showImage"><img src='+imagePreview+' style="width: 216px;height: 384px;text-align:center;" id="imageurl"></div>');
	$("#model").modal("toggle");
}

function updateState(code) {
	var strs= new Array();
	strs=code.split("*");
	var id=strs[0];
	var currentState=strs[1];
	$.ajax({
        url: "/xcr/updateLaunchState.htm",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            "id": id,
            "currentState": currentState
        }),
        success: function (data) {
            if (data.flag == true) {
                win.alert("温馨提示","更改成功",null);
                var options = $('#launchPage-table').bootstrapTable('refresh', {pageNumber:1},{
                    query: {
                        pageIndex: 1,
                        pageSize: 5
                    }
                });
                return true;
            } else {
            	win.alert("温馨提示","更改失败",null);
            	return false;
            }
        }
    });
}
function showEnlargeImg(obj) {
    $(".triggerPicShowEnlarge").each(function () {
        $(this).powerFloat({
            targetMode: "ajax",
            width: "216px",
            height:"384px"
        })
    });
    $(obj).powerFloat({
        targetMode: "ajax",
        width: "216px",
        height:"384px"
    });
}
function saveLaunch(){
	var imagePreview = $("#imageurl")[0].src;
	var id = $("#launchid").val();
	var launchAURL = $("#aImageURL").val().trim();
	$.ajax({
        url: "/xcr/saveLaunchImage.htm",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            "id": id,
            "imagePreview": imagePreview,
            "launchAURL": launchAURL
        }),
        success: function (data) {
            if (data.flag == true) {
            	$('#model').modal('hide');
                win.alert("温馨提示","更改成功",null);
                var options = $('#launchPage-table').bootstrapTable('refresh', {pageNumber:1},{
                    query: {
                        pageIndex: 1,
                        pageSize: 5
                    }
                });
                return true;
            } else {
            	$('#model').modal('hide');
            	win.alert("温馨提示","更改失败",null);
            	return false;
            }
        }
    });
}
//更改图片
$(function () {
	var btn = $("#modifyImage");
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
                win.alert("温馨提示","上传失败",null);
                return false;
            } else {
                var srcUrl = msg.substring(msg.indexOf(':') + 1);
                if (srcUrl) {
                    $("#showImage").html('<img src='+srcUrl+' style="width: 216px;height: 384px;text-align:center;" id="imageurl">');
                } else {
                    win.alert("温馨提示","上传失败!请重新上传",null);
                }
            }
        },
        onChosen: function (file, obj) {
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
            if (imgSize > 1048576) {
            	win.alert("温馨提示","文件大于1M,上传失败!",null);
                obj.outerHTML = '';
                return false;
            }

            return true;
        },
        perviewImageElementId: "fileList", //设置预览图片的元素id
        perviewImgStyle: {width: '164px', height: '164px', border: '1px solid #ebebeb'}//设置预览图片的样式
    });
    var upload = btn.data("uploadFileData");
});
