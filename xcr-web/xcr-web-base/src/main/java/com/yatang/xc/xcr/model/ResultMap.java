package com.yatang.xc.xcr.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yatang.xc.xcr.enums.ErrorMessageList;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月21日 下午5:41:41
 * @Summary :
 */
@JsonSerialize
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResultMap implements Serializable {

    @JsonProperty("mapdata")
    private Object mapdata;

    @JsonProperty("listdata")
    private Page listdata;

    @JsonProperty("Status")
    private Status status;

    /**
     * 返回状态无数据
     */
    public static String success(String state, String message) {
        return ResultMap.fail(state, message);
    }

    /**
     * 返回状态无数据
     */
    public static String fail(String state, String message) {
        return settings(state, message).toString();
    }

    /**
     * 返回状态 设置返回参数
     */
    public static ResultMap successu(String state, Object mapdata) {
        return successu(state).setMapdata(mapdata);
    }

    /**
     * 返回状态 设置返回参数
     */
    public static ResultMap successu(Object mapdata) {
        return successu(SUCCESSFULLY).setMapdata(mapdata);
    }

    /**
     * 返回状态
     */
    public static ResultMap successu() {
        return successu(SUCCESSFULLY);
    }

    /**
     * 返回状态 可根据具体需要 再行设置返回参数
     */
    public static ResultMap successu(String state) {
        return successu(state, "Successfully");
    }

    /**
     * 返回状态 可根据具体需要 再行设置返回参数
     */
    public static ResultMap successu(String state, String message) {
        return settings(state, message);
    }

    /**
     * 返回状态 可根据具体需要 再行设置返回参数
     */
    public static ResultMap faill(String state, String message) {
        return settings(state, message);
    }

    /**
     * 返回状态 可根据具体需要 再行设置返回参数
     */
    public static ResultMap faill(String state, ErrorMessageList message) {
        return settings(state, message.getMessage());
    }

    /**
     * 返回状态 可根据具体需要 再行设置返回参数
     */
    public static ResultMap faill(ErrorMessageList message) {
        return settings(FAILED, message.getMessage());
    }

    /**
     * 返回状态 可根据具体需要 再行设置返回参数
     */
    public static ResultMap failll(ErrorMessageList message) {
        return settings(FAILED, message.getMessage());
    }

    /**
     * 返回状态
     */
    public static ResultMap faill(String state) {
        return faill(state, "Failed");
    }

    /**
     * 返回信息
     */
    public static ResultMap failll(String message) {
        return faill(FAILED, message);
    }

    /**
     * 返回默认信息
     */
    public static ResultMap faill() {
        return faill(FAILED, "Failed");
    }

    /**
     * 返回 默认List集
     */
    public ResultMap listData() {
        this.listdata = new Page(null);
        return this;
    }

    /**
     * 返回 默认List集
     */
    public ResultMap mapData(Object mapdata) {
        this.setMapdata(mapdata);
        return this;
    }

    /**
     * 勿动此方法
     */
    public String toStringEclipse() {
        if (this.mapdata == null) return "{\"Status\":" + this.status + "}";
        return "{\"mapdata\":\"" + this.mapdata + "\",\"Status\":" + this.status + "}  ";
    }

    /**
     * 勿动此方法
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        if (mapdata != null && listdata != null) {
            sb.append("\"mapdata\":").append(mapdata);
            sb.append(",\"listdata\":").append(listdata);
        }

        if (listdata != null && mapdata == null) {
            sb.append("\"listdata\":").append(listdata);
        }

        if (listdata == null && mapdata != null) {
            sb.append("\"mapdata\":").append(mapdata);
        }

        sb.append(",\"Status\":").append(status);
        return sb.append('}').toString();
    }

    public ResultMap setListdata(Object listdata) {
        this.listdata = new Page(listdata);
        return this;
    }

    public Object getListdata() {
        return listdata;
    }

    private static final long serialVersionUID = -7009849295695633778L;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    private final static String FAILED = "M02";
    private final static String SUCCESSFULLY = "M00";

    public Object getMapdata() {
        return mapdata;
    }

    public Status getStatus() {
        return status;
    }

    public ResultMap setStatus(Status status) {
        this.status = status;
        return this;
    }

    public ResultMap page(Integer pageNum, Integer pageSize, Integer totalpage, Long totalcount) {
        if (this.listdata != null) {
            this.listdata.setPageindex((pageNum == null || pageNum == 0) ? 1 : pageNum);
            this.listdata.setPagesize((pageSize == null || pageSize == 0) ? 20 : pageSize);
            this.listdata.setTotalcount((totalcount == null || totalcount == 0L) ? 1 : totalcount);
            this.listdata.setTotalpage((totalpage == null || totalpage == 0) ? 1 : totalpage);
            return this;
        }
        return listData();
    }

    public ResultMap page() {
        if (this.listdata != null) {
            this.listdata.setPageindex(1);
            this.listdata.setPagesize(20);
            this.listdata.setTotalcount(1L);
            this.listdata.setTotalpage(1);
            return this;
        }
        return listData();
    }

    public static ResultMap settings(String state, String message) {
        ResultMap map = new ResultMap();
        Status status = new Status();
        status.setState(state);
        status.setStateID(state.substring(1));
        status.setStateValue(state.substring(1));
        status.setStateDesc(message);
        map.setStatus(status);
        return map;
    }

    public ResultMap setMapdata(Object mapdata) {
        if (mapdata != null) {
            if (mapdata instanceof List) {
                List<?> list = (List<?>) mapdata;
                if (list != null && list.size() > 0) {
                    this.mapdata = mapdata;
                }

            } else if (mapdata instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) mapdata;
                if (map != null && map.size() > 0) {
                    this.mapdata = mapdata;
                }

            } else if (mapdata instanceof Object[]) {
                Object[] array = (Object[]) mapdata;
                if (array != null && array.length > 0) {
                    this.mapdata = mapdata;
                }

            } else {
                this.mapdata = mapdata;
            }
        }
        return this;
    }

}
