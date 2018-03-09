package com.yatang.xc.xcr.dto.outputs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @since  2.6
 * @Author : BobLee
 * @CreateTime : 2017年12月08日 15:10
 * @Summary : 分类列表
 */
@SuppressWarnings("serial")
public class NewClassifyListResultSetDto implements Serializable{
    
    /**
      * 分类Id
      */
    @JsonProperty("ClassifyId")
    private String  classifyId;

    /**
     *分类名称
     */
    @JsonProperty("ClassifyName")
    private String  classifyName;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"classifyId\":\"").append(classifyId).append('\"');
        sb.append(",\"classifyName\":\"").append(classifyName).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }
}
