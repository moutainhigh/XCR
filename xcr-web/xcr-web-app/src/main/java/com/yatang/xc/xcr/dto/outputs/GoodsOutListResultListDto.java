package com.yatang.xc.xcr.dto.outputs;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 下午6:02:33
 * @Summary :
 */
@SuppressWarnings("serial")
public class GoodsOutListResultListDto implements Serializable {

	@JsonProperty("rows")
	private List<GoodsOutListResultDto> rows;

	@JsonProperty("pageindex")
	private Integer pageindex;

	@JsonProperty("pagesize")
	private Integer pagesize;

	@JsonProperty("totalpage")
	private Integer totalpage;

	@JsonProperty("totalcount")
	private Long totalcount;

	@SuppressWarnings("unchecked")
	public static GoodsOutListResultListDto none() {
		GoodsOutListResultListDto r = new GoodsOutListResultListDto();
		r.setPageindex(1);
		r.setPagesize(20);
		r.setTotalcount(0L);
		r.setTotalpage(1);
		r.setRows(Collections.EMPTY_LIST);
		return r;
	}

	public List<GoodsOutListResultDto> getRows() {
		return rows;
	}

	public void setRows(List<GoodsOutListResultDto> rows) {
		this.rows = rows;
	}

	public Integer getPageindex() {
		return pageindex;
	}

	public void setPageindex(Integer pageindex) {
		this.pageindex = pageindex;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

	public Integer getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(Integer totalpage) {
		this.totalpage = totalpage;
	}

	public Long getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}

	@Override
	public String toString() {
		return "{\"rows\":\"" + rows + "\",\"pageindex\":\"" + pageindex + "\",\"pagesize\":\"" + pagesize + "\",\"totalpage\":\"" + totalpage + "\",\"totalcount\":\"" + totalcount + "\"}  ";
	}

}
