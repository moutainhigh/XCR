package com.yatang.xc.xcr.vo;

import java.util.Date;

/**
 * Created by wangyang on 2017/6/26.
 */
public class ClassListVO {

    private String isMission; //任务
    private Long id; //主键ID
    private String name; //课程名称
    private String content; //内容
    private Integer trainLength; //建议培训时长
    private String icon; //课程图标
    private String status; //状态：0未发布,1已发布
    private String viedioUrl; //视屏链接url
    private String imagesUrl; //图片url(多图以,拼接)
    private String fileUrl; //上传文件服务器文件地址
    private String remark; //备注
    private Integer createUid; //创建人id
    private Date createTime; //创建时间
    private Integer modifyUid; //修改人id
    private String modifyTime; //最后修改时间
    private Date releasesTime; //发布时间

    public String getIsMission() {
        return isMission;
    }

    public void setIsMission(String isMission) {
        this.isMission = isMission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTrainLength() {
        return trainLength;
    }

    public void setTrainLength(Integer trainLength) {
        this.trainLength = trainLength;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getViedioUrl() {
        return viedioUrl;
    }

    public void setViedioUrl(String viedioUrl) {
        this.viedioUrl = viedioUrl;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
