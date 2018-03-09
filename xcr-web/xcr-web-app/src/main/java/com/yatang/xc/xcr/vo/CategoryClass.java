package com.yatang.xc.xcr.vo;

public class CategoryClass {
	private String id;
	private String category_name;
	private Integer timestamp;
	private String secondList;

	private String classifySecondId;
	private String classifySecondName;

	public String getSecondList() {
		return secondList;
	}

	public void setSecondList(String secondList) {
		this.secondList = secondList;
	}

	public String getClassifySecondId() {
		return classifySecondId;
	}

	public void setClassifySecondId(String classifySecondId) {
		this.classifySecondId = classifySecondId;
	}

	public String getClassifySecondName() {
		return classifySecondName;
	}

	public void setClassifySecondName(String classifySecondName) {
		this.classifySecondName = classifySecondName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

}
