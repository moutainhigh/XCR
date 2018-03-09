package com.yatang.xc.xcr.biz.train.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 课堂信息PO
 * @author huangjianjun
 */
public class TrainInfoDTO implements Serializable{
	
	private static final long serialVersionUID = -4100106548699191610L;
	/**主键ID*/
	private Long id;

	/**课程名称*/
	private String name;
	
	/**内容*/
	private String content;
	
	/**建议培训时长*/
	private Integer trainLength;
	
	/**课程图标*/
	private String icon;
	
	/**状态：0未发布,1已发布*/
	private Integer status;
	
	/**视屏链接url*/
	private String viedioUrl;
	
	/**图片url(多图以,拼接)*/
	private String imagesUrl;
	
	/**上传文件服务器文件地址*/
	private String fileUrl;
	
	/**备注*/
	private String remark;
	
	/**创建人id*/
	private Integer createUid;
	
	/**创建时间*/
	private Date createTime;
	
	/**修改人id*/
	private Integer modifyUid;
	
	/**最后修改时间*/
	private Date modifyTime;
	
	/**发布时间*/
	private Date releasesTime;
	
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	@Override
	public String toString() {
		return "TrainInfoDTO [id=" + id + ", name=" + name + ", content="
				+ content + ", trainLength=" + trainLength + ", icon=" + icon
				+ ", status=" + status + ", viedioUrl=" + viedioUrl
				+ ", imagesUrl=" + imagesUrl + ", fileUrl=" + fileUrl
				+ ", remark=" + remark
				+ ", createUid=" + createUid + ", createTime=" + createTime
				+ ", modifyUid=" + modifyUid + ", modifyTime=" + modifyTime
				+ ", releasesTime=" + releasesTime + "]";
	}
}
