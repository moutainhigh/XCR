package com.yatang.xc.xcr.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 新页面消息列表
 * <p>
 * Created by wangyang on 2017/6/24.
 */
public class MsgPushVO implements Serializable{

    private static final long serialVersionUID = 3536917491016635612L;

    private Long id; //主键ID
    private String title; //消息标题
    private String imageUrl; //消息图片地址
    private String msgUrl; //消息地址
    private String status; //状态：0未发布,1已发布
    private String type; //消息类型0：通知1:警告
    private Integer createUid; //创建人id
    private Date createTime; //创建时间(时间戳)
    private Integer modifyUid; //修改人id
    private String modifyTime; //最后修改时间(时间戳)
    private Date releasesTime; //发布时间
    private String pushType;//发送类型 0：所有 1：定向
    private String pushTo;//推送对象

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushTo() {
        return pushTo;
    }

    public void setPushTo(String pushTo) {
        this.pushTo = pushTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Integer createUid) {
        this.createUid = createUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getModifyUid() {
        return modifyUid;
    }

    public void setModifyUid(Integer modifyUid) {
        this.modifyUid = modifyUid;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getReleasesTime() {
        return releasesTime;
    }

    public void setReleasesTime(Date releasesTime) {
        this.releasesTime = releasesTime;
    }

    @Override
    public String toString() {
        return "MsgPushVO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", msgUrl='" + msgUrl + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", createUid=" + createUid +
                ", createTime=" + createTime +
                ", modifyUid=" + modifyUid +
                ", modifyTime='" + modifyTime + '\'' +
                ", releasesTime=" + releasesTime +
                ", pushType='" + pushType + '\'' +
                ", pushTo='" + pushTo + '\'' +
                '}';
    }
}
