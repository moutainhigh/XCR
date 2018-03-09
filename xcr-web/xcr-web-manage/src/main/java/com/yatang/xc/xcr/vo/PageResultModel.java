package com.yatang.xc.xcr.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangyang on 2017/6/21.
 */
public class PageResultModel<T> implements Serializable{

    private Integer rows;
    private Integer total;
    private List<T> data;

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
