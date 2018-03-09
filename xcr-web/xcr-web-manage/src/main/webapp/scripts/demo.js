/**
 * AdminLTE Demo Menu
 * ------------------
 * You should not use this file in production.
 * This file is for demo purposes only.
 */
(function ($, AdminLTE) {

    "use strict";

    /**
     * Store a new settings in the browser
     *
     * @param String name Name of the setting
     * @param String val Value of the setting
     * @returns void
     */
    function store(name, val) {
        if (typeof (Storage) !== "undefined") {
            localStorage.setItem(name, val);
        } else {
            window.alert('Please use a modern browser to properly view this template!');
        }
    }

    /**
     * Get a prestored setting
     *
     * @param String name Name of of the setting
     * @returns String The value of the setting | null
     */
    function get(name) {
        if (typeof (Storage) !== "undefined") {
            return localStorage.getItem(name);
        } else {
            window.alert('Please use a modern browser to properly view this template!');
        }
    }
})(jQuery, $.AdminLTE);


function loadHtml(path, data) {
	if(path.substring(0,$.ctx.length)==$.ctx){
		$("#contentHtml").load(path);
	}else{
		$("#contentHtml").load($.ctx+path);
	}
    window.customData = data;
}

/*通过代理获取网页内容 并把所有内容中的相对地址替换成绝对地址*/
function loadHtml_poxy(path, data) {
	$("#contentHtml").load($.ctx+"/sys/loadhtml/replaceURL.htm",{"url":path});
}

function loadAjax(path,htmldata,data,callback) {
    if(!callback){
        callback = function () {}
    }
    $("#contentHtml").load(path,data,callback);
    window.customData = htmldata;
}
function smoothscrollToTop(){
    var currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
    if (currentScroll > 0) {
        window.requestAnimationFrame(smoothscrollToTop);
        window.scrollTo (0,currentScroll - (currentScroll/10));
    }
}


function getElementLeft(element) {
    var actualLeft = element.offsetLeft;
    var current = element.offsetParent;
    while (current !== null) {
        actualLeft += current.offsetLeft;
        current = current.offsetParent;
    }
    return actualLeft;
}
function getElementTop(element) {
    var actualTop = element.offsetTop;
    var current = element.offsetParent;
    while (current !== null) {
        actualTop += current.offsetTop;
        current = current.offsetParent;
    }
    return actualTop;
}

function getScrollWidth() {
    var noScroll, scroll, oDiv = document.createElement("DIV");
    oDiv.style.cssText = "position:absolute; top:-1000px; width:100px; height:100px; overflow:hidden;";
    noScroll = document.body.appendChild(oDiv).clientWidth;
    oDiv.style.overflowY = "scroll";
    scroll = oDiv.clientWidth;
    document.body.removeChild(oDiv);
    return noScroll - scroll;
}

function getLocalTime(nS) {
    var time = new Date(nS);
    var y = time.getFullYear();//年
    var m = time.getMonth() + 1;//月
    var d = time.getDate();//日
    var h = time.getHours();//时
    var mm = time.getMinutes();//分
    var s = time.getSeconds();//秒
    return y+"-"+m+"-"+d+" "+h+":"+mm+":"+s;
}

function setRequiredData(formData,auditData) {
    formData.push({
        name:'taskId',
        value:auditData.taskId
    });
    formData.push({
        name:"processInstanceId",
        value:auditData.processInstanceId
    });

    formData.push({
        name:"taskDefId",
        value:auditData.taskDefId
    });

    formData.push({
        name:"processType",
        value:auditData.processType
    });

    formData.push({
        name:"intentionId",
        value:auditData.intentionId
    });

    formData.push({
        name:"formId",
        value:auditData.formId
    });
    formData.push({
        name:"originatorId",
        value:auditData.originatorId
    });
    formData.push({
        name:"originatorName",
        value:auditData.originatorName
    });
    formData.push({
        name:"telephone",
        value:auditData.telephone
    });
    formData.push({
        name:"taskDefName",
        value:auditData.taskDefName
    });
    formData.push({
        name:"companyId",
        value:auditData.companyId
    });
}

function asyncSubmitForm(e) {
    var options = {
        beforeSubmit: e.data.beforeSubmit,  //提交前的回调函数
        success: passSuccess,      //提交后的回调函数
        timeout:30000               //限制请求的时间，当请求大于3秒后，跳出请求
    }
    $("#"+e.data.submitFormId).ajaxSubmit(options);
    var superE = e;
    function passSuccess(data, statusText){
        if(statusText == 'success'){
            hideModalAndShowAnother(e.data.submitModelId,function (e) {
                if(data.success == true){
                    loadHtml(superE.data.loadHtml);
                }else{
                    // $("#dataTableErrorCodeModal").find("#errorCode").html(data.code);
                    $("#dataTableErrorCodeModal").find("#errorMessage").html(data.errorMessage);
                    $("#dataTableErrorCodeModal").modal("show")
                    $("#dataTableErrorCodeModalButton").on('click',function () {
                        setTimeout(function () {
                            loadHtml(superE.data.loadHtml);
                        },500);
                    })

                }

            });
        }
    }
}

function renderDiagram(node,taskNodeMap,diagram,current) {
    var isCurrent = current;
    if(current == undefined){
        isCurrent = 0;
    }
    while (node){
        var type = node.type;
        var taskDefName;
        if(type == "start"){
            taskDefName = "开始";
        }else if(type == "end"){
            taskDefName = "结束";
        }else{
            taskDefName = node.taskDefName;
        }

        var status = "finished";
        if(node.active == true && isCurrent == 0){
            status = "current"
            isCurrent = 1;
        }else if(isCurrent == 1){
            status = "normal"
        }

        var diagramNode = diagram.createNode(status,{
            title:taskDefName,
            realNames:node.realNames,
            id:node.key
        })

        console.log(node.taskDefName);
        var linesKey = node.linesKey;
        node = taskNodeMap[linesKey[0]]
        if(linesKey.length > 1){
            for(var i = 1;i < linesKey.length;i++){
                var dzNode = taskNodeMap[linesKey[i]]
                var fzDiagram = diagramNode.createFzDiagram(status);
                renderDiagram(dzNode,taskNodeMap,fzDiagram,isCurrent);
            }
        }
    }
}


function createDiagramObject(id,x,y,setting) {
    // var width = $("#"+id).width();
    var height = $("#"+id).height();
    var paper=Raphael(id,setting.cWidth,height);//在 id 为 flowshow 元素中创建画布
    return diagramObject(x,y,paper,setting);
}

function diagramObject(x,y,paper,setting) {
    var currentX = x;
    var currentY = y;
    var index = 0;
    var setting = setting;
    if(setting == undefined || setting == null){
        setting = {
            lineLength:50
        }
    }
    return {
        createPath:function (length,status) {
            var lxX = currentX + length;
            var lxY = currentY;
            var c = paper.path("M"+currentX+" "+currentY+"L"+lxX+" "+lxY);
            currentX = lxX;
            var color = "#cccccc";
            if(status == 'finished'){
                color = "red"
            }else if(status == 'normal'){
                color = "#cccccc"
            }else if(status == 'current'){
                color = 'red'
            }
            c.attr({
                "stroke": color,
                "stroke-width": 3,
            });
            c.attr("opacity", "0.5");
        },
        createNode:function (status,data) {
            if(index > 0){
                this.createPath(setting.lineLength,status);
            }
            var r = setting.cirR;
            //确定当前坐标系
            var cxX = currentX + r + 1;
            var cxY = currentY;


            var color = "#cccccc";
            var stColor = "black";
            var stOpacity = 0.2;
            var opacity = 1;
            if(status == 'current'){
                color = "red";
                stColor = "red";
                stOpacity = 1;
            }else if(status == 'finished'){
                color = "red"
                opacity = 0.5;
            }else if(status == 'normal'){
                color = "#cccccc"
            }
            //画圆
            var circleA = paper.circle(cxX, cxY, r);
            circleA.attr({
                "stroke": stColor,
                "stroke-width": 0.6,
                "fill": "#ffffff",
            });
            circleA.attr("opacity", stOpacity);
            var circleB = paper.circle(cxX, cxY, r-3);
            circleB.attr({
                "fill": color,
                "stroke": color,
                "stroke-width": 0.5,
                "cursor":"pointer"
            });
            circleB.attr("opacity", opacity);
            // circleA.click(circleClick);

            function circleClick(e) {
                var auditMessages = window.auditMessages;
                var auditUMs = auditMessages[data.id];
                if(!auditUMs){
                    return;
                }
                var auditTemplate = "";
                for(var i = 0;i < auditUMs.length;i++){
                    var color = "green";
                    if(auditUMs[i].type == 2){
                        color = "gray";
                    }else if(auditUMs[i].type == 1){
                        color = "red";
                    }
                    var auditMessage = '<div style="padding-top:4px;padding-bottom: 4px;"><div style="display: inline-block;"><span style="display:inline-block;width:14px;height: 14px;border-radius:7px;background-color: '+color+';margin-right:4px;word-wrap :break-word;"></span><span style="margin-right:8px;display: inline-block;">'+auditUMs[i].auditorName+':</span></div><span style="display: inline-block;word-wrap :break-word;width:100%;">'+auditUMs[i].auditMessage+'</span></div>'
                    auditTemplate = auditTemplate + auditMessage;
                }

                $("#auditMessageMadol").html(auditTemplate);
                $("#auditMessageMadolHeader").html(data.title);
                $("#auditMessageMadolContainer").modal('show')
            }
            circleB.click(circleClick)

            if(data != undefined && data != null && data.title != undefined){
                var title = paper.text(currentX+r, currentY + r + 12, data.title);
                title.attr({
                    "stroke-width": 2,
                });
            }

            if(data != undefined && data != null && data.realNames != undefined){
                var kk = 16;
                for(var i = 0;i < data.realNames.length;i++){
                    var body = paper.text(currentX+r, currentY + r + 12 + kk, data.realNames[i]);
                    kk = (i+2)*16
                    body.attr({
                        "stroke-width": 1,
                    });
                }
            }
            index++;
            //重置流程图坐标系
            currentX = cxX + r + 1;
            currentY = cxY;
            return diagramNode(currentX-r,currentY-r,paper,setting);
        }
    }
}

function diagramNode(x,y,paper,setting) {
    var currX = x;
    var currY = y;
    var topNodes = [];
    var bottomNodes = [];

    return {
        createFzDiagram:function (status) {
            var length = setting.zLineLength;
            var topX = currX;
            var topY = currY - length;
            var top2X = currX + length;
            var top2Y = topY
            //画一条折线
            var c = paper.path("M"+currX+" "+currY+"L"+topX+" "+topY+"M"+topX+" "+topY+"L"+top2X+" "+top2Y);
            var color = "#cccccc";
            if(status == 'finished'){
                color = "red"
            }else if(status == 'normal'){
                color = "#cccccc"
            }
            c.attr({
                "stroke": color,
                "stroke-width": 3,
            });
            c.attr("opacity", "0.5");
            return diagramObject(top2X,top2Y,paper,setting);
        }
    }
}

function showDiagramViewNew(id,data) {
    var active = data.activeId;
    var tasks = data.tasks;
    var activeIndex;
    var nodeTemplate;
    var template = '<ol class="ui-step ui-step-red ui-step-'+tasks.length+'">';
    for(var i = 0;i < tasks.length;i++) {
        var classValue;
        if(active == tasks[i].id){
            activeIndex = i
        }
        if(activeIndex == undefined){
            if(i == 0){
                classValue = "step-start step-done";
            }else{
                classValue = "step-done";
            }
        }
        if(activeIndex != undefined){
            classValue = $.trim(classValue) + " step-active";
        }
        if(activeIndex != undefined && i > activeIndex){
            classValue = "";
        }
        if(i == tasks.length - 1) {
            classValue = classValue + " step-end";
        }
        var realNamesTemplate = "";
        var realNames = tasks[i].assignUser.realNames;
        if(realNames){
            for(var j = 0;j < realNames.length;j++){
                var m = '<div>'+realNames[j]+'</div>';
                realNamesTemplate = realNamesTemplate + m;
            }
        }
        nodeTemplate = '<li class="'+classValue+'">'+
            '<div class="ui-step-line"></div>'+
            '<div class="ui-step-cont">'+
            '<span class="ui-step-cont-number pressButton" style="cursor: pointer" data-title="'+tasks[i].name+'" data-taskDefId="'+tasks[i].id+'" data-placement="bottom">'+(i+1)+'</span>'+
            '<div class="ui-step-cont-text">' +
                '<div>' + tasks[i].name + '</div>'+
                '<div>' + realNamesTemplate + '</div>'+
            '</div>'+
            '</div>'+
            '</li>';
        template = template + nodeTemplate;
    }
    template = template + '</ol>';
    $("#"+id).html(template);

    $(".pressButton").on("mouseenter",function () {
        var title = $(this).attr("data-title");
        var taskDefId = $(this).attr("data-taskDefId");
        var auditMessages = window.auditMessages;
        var auditUMs = auditMessages[taskDefId];
        if(!auditUMs){
            return;
        }
        var auditTemplate = "";
        for(var i = 0;i < auditUMs.length;i++){
            var color = "green";
            if(auditUMs[i].type == 2){
                color = "gray";
            }else if(auditUMs[i].type == 1){
                color = "red";
            }
            var auditMessage = '<div style="padding-top:4px;padding-bottom: 4px;"><span style="display:inline-block;width:14px;height: 14px;border-radius:7px;background-color: '+color+';margin-right:4px;"></span><span style="margin-right:8px;">'+auditUMs[i].auditorName+':</span><span>'+auditUMs[i].auditMessage+'</span></div>'
            auditTemplate = auditTemplate + auditMessage;
        }
        $(this).popover({
            html:true,
            content:'<div style="color: black;width: 184px;word-wrap:break-word;">'+auditTemplate+'</div>',
            title:'<span style="color: black;">'+title+'</span>'
        });
        $(this).popover('show');
    });
    $(".pressButton").on("mouseleave",function () {
        $(this).popover('hide');
    });
}

function showDiagramView(id,data,index,callback) {
    if(callback == undefined){
        callback = function () {}
    }
    if(index == undefined){
        index="";
    }
    var active = data.activeId == '#'?'yatang_subsidiary_generalmanager':data.activeId;
    var tasks = data.tasks;
    var root = '<div style="background: grey;height:2px;width: '+(tasks.length*17 - 3)+'rem;position: relative;top:10%;">';
    var activeIndex;
    var ik = 0;
    for(var i = 0;i < tasks.length;i++){
        var color;
        if(active == tasks[i].id){
            color = "red";
            activeIndex = i+"-"+tasks[i].id;
        }else{
            color = "#ffffff";
        }

        var realNamesTemplate = "";
        var realNames = tasks[i].assignUser.realNames;
        if(realNames){
            for(var j = 0;j < realNames.length;j++){
                var m = '<div>'+realNames[j]+'</div>';
                realNamesTemplate = realNamesTemplate + m;
            }
        }

        if(activeIndex){
            ik++;
        }

        if(ik <= 1){
            var node = '<div style="position: absolute;margin-top:-1rem;margin-left: '+(i*17)+'rem;">'+
                '<div id="'+index+'task'+i+'-'+tasks[i].id+'" class="taskHeadNode" style="border-radius:50%;width: 2rem;height: 2rem;border:1px solid #97a0b3;background: '+color+';margin:0 auto"></div>'+
                '<div style="width: 14rem;height: 14rem;background: #ff9900;margin-top: 1rem;box-shadow: 1px 1px 4px #888888;">' +
                '<div style="width: 100%;height: 2.7rem;background: coral;line-height: 2.7rem;padding-left:1rem;padding-right: 1rem;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">'+tasks[i].name+'</div>'+
                '<div style="padding-left: 1rem;padding-right: 1rem;padding-top: 1rem;">' +
                realNamesTemplate+
                '</div>'+
                '</div>'+
                '</div>'
            root = root + node;
        }
    }
    root = root+'</div>';
    $("#"+id).css("height","21.5rem");
    $("#"+id).html(root);
    $(".taskHeadNode").on('click',{
        activeNode:index+"task"+activeIndex
    },callback);
}

/**
 * 生成表单模板
 * @param data
 */
function renderFormData(data) {
    if(!data){
        return;
    }
    data = JSON.parse(data);
    console.log(data);
    var formTemplate = '';
    var flag = false;
    for(var i = 0;i < data.length;i++){
        if(i % 2 == 0 && flag == false){
            formTemplate = formTemplate + '<div class="row">';
            flag = true;
        }else if(i % 2 == 0 && flag == true){
            formTemplate = formTemplate + '</div>';
            formTemplate = formTemplate + '<div class="row">';
            flag = false;
        }
        var inputTemplate = '<div class="form-group col-md-6"><label for="verifySuggestion'+i+'">'+
            data[i]["label"]+
            ':</label><input id="verifySuggestion'+i+'" type="'+
            data[i]["type"]+
            '" name="businessData['+
            data[i]["name"]+
            ']" value="'+
            data[i]["defaultValue"]+
            '" class="form-control" placeholder="'+data[i]["placeholder"]+'"/></div>';
        formTemplate = formTemplate + inputTemplate;
        if(i == data.length-1 && data.length % 2 != 0){
            formTemplate = formTemplate + '<div class="col-md-6"></div></div>';
        }
    }
    return formTemplate;
}

/**
 * 隐藏一个modal显示另一个并处理一些事情
 * @param hideModalId
 * @param callback
 */
function hideModalAndShowAnother(hideModalId,callback) {
    var modal = $('#'+hideModalId);
    modal.modal("hide");
    modal.on('hidden.bs.modal', function (k) {
        callback(k);
        modal.unbind('hidden.bs.modal');
    })
}

