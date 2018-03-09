package com.yatang.xc.xcr.biz.core.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangyang on 2017/7/10.
 */
public class UserSignSetDTO implements Serializable{

    private static final long serialVersionUID = -4101211119806961392L;
    private Long id;
    private Integer type;
    private String data;
    private String content;
    private Integer isDelete;
    private Date createTime;
    private Date updateTime;

    public UserSignSetDTO() {
    }

    public UserSignSetDTO(Integer type, String data, String content) {
        this.type = type;
        this.data = data;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public String toString() {
        return "UserSignSetDTO{" +
                "id=" + id +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", content='" + content + '\'' +
                ", isDelete=" + isDelete +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
