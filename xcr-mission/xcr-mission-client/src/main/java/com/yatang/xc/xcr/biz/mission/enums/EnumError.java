package com.yatang.xc.xcr.biz.mission.enums;

/**
 * 
 * 奖励类型枚举
 * @author : zhaokun
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  zhaokun 
 */
public enum EnumError {
    ERROR_SYSTEM_EXCEPTION("SYSERROR999", "系统错误"),
    ERROR_BUSINESS_RELATED_PUBLISH("BUSERROR001", "关联任务不能单独发布"),
    ERROR_BUSINESS_RELATED_CREATE("BUSERROR002", "关联任务不能是周期性任务"),
    ERROR_BUSINESS_RELATED_EXECUTE("BUSERROR003", "关联任务还不满足条件"),
    ERROR_BUSINESS_NEED_MANUAL_AUDIT("BUSERROR004", "任务需要人工审核"),
    ERROR_BUSINESS_EXECUTE_UNFINISHED("BUSERROR005", "任务不是完成状态"),
    ERROR_BUSINESS_NOT_NEED_MANUAL_AUDIT("BUSERROR006", "任务不需要人工审核"),
    ERROR_BUSINESS_PUBLISH_NOT_UPDATE("BUSERROR007", "已发布不能修改"),
    ERROR_BUSINESS_NOT_RELATED("BUSERROR008", "不能关联的任务"),
    ERROR_BUSINESS_EXECUTE_NOT_INIT("BUSERROR009", "任务已经不是初始化或未完成状态"),
    ERROR_BUSINESS_MISSION_TEMPLATE_ERROR("BUSERROR010", "任务模板不存在"),
    ERROR_BUSINESS_RELATED_NOT_REMOVE("BUSERROR011", "已发布不能删除"),
    ERROR_BUSINESS_MISSION_NOT_EXIST("BUSERROR012", "任务不存在"),
    ERROR_BUSINESS_EXECUTE_DATA_ERROR("BUSERROR013", "参数校验不通过"),
    ERROR_BUSINESS_PUBLISH_NOT_REMOVE("BUSERROR014", "已发布不能删除"),
    ERROR_BUSINESS_MISSION_EXPIRED("BUSERROR015", "任务已经过期"),
    ERROR_BUSINESS_BPM_FAILE("BUSERROR016", "bpm流程启动失败"),
    ERROR_BUSINESS_NAME_CHECK_ERROR("BUSERROR017", "任务名称不能重复"),
    ERROR_BUSINESS_COURSEID_DUPLICATE("BUSERROR018", "课程已存在任务"),
    AWARD_TYPE_NONE("NONE", "无");
    

    private String code;

    private String message;


    EnumError(String code,String message){
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

}
