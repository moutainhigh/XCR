package com.yatang.xc.xcr.biz.core.domain;

import java.util.Date;

/**
 * 签到设置PO
 * Created by wangyang on 2017/7/7.
 */
public class UserSignSetPO {

    private Long id;
    private int type;        //签到类型
    private String data;     //奖励数额
    private String content;  //签到说明
    private int isDelete;    //是否删除
    private Date createTime; //创建时间
    private Date updateTime; //更新时间

    public UserSignSetPO() {
    }

    public UserSignSetPO(int type, String data, String content) {
        this.type = type;
        this.data = data;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
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
        return "UserSignSetPO{" +
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
