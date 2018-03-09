package com.yatang.xc.xcr.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月11日 10:24
 * @Summary : List
 */
@JsonSerialize
@SuppressWarnings("serial")
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Page implements Serializable {

    @JsonProperty("rows")
    private Object rows;

    @JsonProperty("pageindex")
    private Integer pageindex;

    @JsonProperty("pagesize")
    private Integer pagesize;

    @JsonProperty("totalpage")
    private Integer totalpage;

    @JsonProperty("totalcount")
    private Long totalcount;

    public Page() { }

    public Page(List<Object> rows) {
        if (rows == null || rows.size() == 0) {
            this.pageindex = 1;
            this.pagesize = 20;
            this.totalcount = 0L;
            this.totalpage = 1;
            this.rows = Collections.EMPTY_LIST;
        }else{
            this.rows = rows;
        }
    }

    public Page(Object rows) {
        if (rows == null) {
            this.pageindex = 1;
            this.pagesize = 20;
            this.totalcount = 0L;
            this.totalpage = 1;
            this.rows = Collections.EMPTY_LIST;
        }
        if (rows instanceof List) {
            this.rows = (List<Object>) rows;
        }
        else{
            this.rows = Arrays.asList(rows);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"rows\":").append(rows);
        sb.append(",\"pageindex\":").append(pageindex);
        sb.append(",\"pagesize\":").append(pagesize);
        sb.append(",\"totalpage\":").append(totalpage);
        sb.append(",\"totalcount\":").append(totalcount);
        sb.append('}');
        return sb.toString();
    }

    public static Page setRowss(List<Object> rows) {
        return new Page(rows);
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(List<Object> rows) {
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
}
