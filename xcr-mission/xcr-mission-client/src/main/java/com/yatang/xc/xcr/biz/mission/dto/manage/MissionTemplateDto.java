package com.yatang.xc.xcr.biz.mission.dto.manage;

import java.io.Serializable;

public class MissionTemplateDto implements Serializable {

    /**
     * @Fields serialVersionUID : TODO 变量名称
     */
    private static final long serialVersionUID = -2723776416500725033L;


    @Override
    public String toString() {
        return "MissionTemplateDto [name=" + name + ", type=" + type + ", templateCode=" + templateCode + "]";
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 模板编码
     */
    private String templateCode;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getTemplateCode() {
        return templateCode;
    }


    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

}
