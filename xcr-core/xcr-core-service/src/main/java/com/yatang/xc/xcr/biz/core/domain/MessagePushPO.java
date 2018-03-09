package com.yatang.xc.xcr.biz.core.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @param <T>
 * @描述: 消息推送PO
 * @作者: huangjianjun
 * @创建时间: 2017年3月28日-下午3:31:51 .
 * @版本: 1.0 .
 */
public class MessagePushPO implements Serializable {

    private static final long serialVersionUID = 7936152780486089777L;

    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息图片地址
     */
    private String imageUrl;

    /**
     * 消息地址
     */
    private String msgUrl;

    /**
     * 状态：0未发布,1已发布
     */
    private String status;

    /**
     * 消息类型0：通知1:警告
     */
    private String type;

    /**
     * 创建人id
     */
    private Integer createUid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人id
     */
    private Integer modifyUid;

    /**
     * 最后修改时间
     */
    private Date modifyTime;

    /**
     * 发布时间
     */
    private Date releasesTime;

    /**
     * 发送类型 0：所有 1：定向
     **/
    private String pushType;

    /**
     * 推送对象
     **/
    private String pushTo;

    /**
     * 区域推送 多个以,隔开
     **/
    private String areaStr;

    /**消息编辑内容**/
    private String contentHtml;

    /** -1链接，1编辑内容 **/
    private String contentFrom;

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public String getContentFrom() {
        return contentFrom;
    }

    public void setContentFrom(String contentFrom) {
        this.contentFrom = contentFrom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaStr() {
        return areaStr;
    }

    public void setAreaStr(String areaStr) {
        this.areaStr = areaStr;
    }

    public String getPushTo() {
        return pushTo;
    }

    public void setPushTo(String pushTo) {
        this.pushTo = pushTo;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
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


    public Integer getModifyUid() {
        return modifyUid;
    }

    public void setModifyUid(Integer modifyUid) {
        this.modifyUid = modifyUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
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
        return "MessagePushPO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", msgUrl='" + msgUrl + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", createUid=" + createUid +
                ", createTime=" + createTime +
                ", modifyUid=" + modifyUid +
                ", modifyTime=" + modifyTime +
                ", releasesTime=" + releasesTime +
                ", pushType='" + pushType + '\'' +
                ", pushTo='" + pushTo + '\'' +
                ", areaStr='" + areaStr + '\'' +
                '}';
    }
}
