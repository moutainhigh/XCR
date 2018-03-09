package com.yatang.xc.xcr.dto.outputs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月29日 14:43
 * @Summary : List<LogisticsInfoListResultSetDto::List<LogisticsSec>>
 */
@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogisticsInfoListResultSetDto implements Serializable {

    /**
     * 以下是二级列表数据
     */
    @JsonProperty("LogisticsSecList")
    private List<LogisticsSec> logisticsSecList;

    /**
     * 物流单号
     */
    @JsonProperty("LogisticsNo")
    private String logisticsNo;

    /**
     * 物流公司名称
     */
    @JsonProperty("LogisticsName")
    private String logisticsName;

    /**
     * 二级列表数据
     */
    public static class LogisticsSec implements Serializable {

        /**
         * 物流地址
         */
        @JsonProperty("LogisticsAddress")
        private String logisticsAddress;

        /**
         * 物流时间
         * 时间戳
         */
        @JsonProperty("LogisticsTime")
        private Long logisticsTime;

        public String getLogisticsAddress() {
            return logisticsAddress;
        }

        public void setLogisticsAddress(String logisticsAddress) {
            this.logisticsAddress = logisticsAddress;
        }

        public Long getLogisticsTime() {
            return logisticsTime;
        }

        public void setLogisticsTime(Long logisticsTime) {
            this.logisticsTime = logisticsTime;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"LogisticsAddress\":\"").append(StringUtils.isBlank(logisticsAddress) ? "" : logisticsAddress).append('\"');
            sb.append(",\"LogisticsTime\":\"").append( logisticsTime).append('\"');
            sb.append('}');
            return sb.toString();
        }
    }

    public List<LogisticsSec> getLogisticsSecList() {
        return logisticsSecList;
    }

    public void setLogisticsSecList(List<LogisticsSec> logisticsSecList) {
        this.logisticsSecList = logisticsSecList;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    /**
     * 空列表
     */
    public static LogisticsInfoListResultSetDto none() {
        LogisticsInfoListResultSetDto none = new LogisticsInfoListResultSetDto();
        none.setLogisticsName("");
        none.setLogisticsNo("");
        none.setLogisticsSecList(Collections.EMPTY_LIST);
        return none;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"LogisticsSecList\":").append((logisticsSecList == null || logisticsSecList.size() == 0 ? Collections.EMPTY_LIST : logisticsSecList));
        sb.append(",\"LogisticsNo\":\"").append((StringUtils.isBlank(logisticsNo) ? "" : logisticsNo)).append('\"');
        sb.append(",\"LogisticsName\":\"").append((StringUtils.isBlank(logisticsName) ? "" : logisticsName)).append('\"');
        sb.append('}');
        return sb.toString();
    }


    /**
     * 测试数据集
     * */
//    public static void main(String[] args) {
//        java.util.List<LogisticsInfoListResultSetDto> list = new  java.util.ArrayList<LogisticsInfoListResultSetDto>(3);
//        for(int i =0 ; i< 3 ; i++){
//            LogisticsInfoListResultSetDto n = new LogisticsInfoListResultSetDto();
//            n.setLogisticsNo(System.currentTimeMillis()+"");
//            n.setLogisticsName("顺丰"+i);
//            java.util.List<LogisticsInfoListResultSetDto.LogisticsSec> listLogisticsSec = new  java.util.ArrayList<LogisticsInfoListResultSetDto.LogisticsSec>(2);
//            for(int j = 0 ; j < 2 ; j++){
//                LogisticsInfoListResultSetDto.LogisticsSec sec = new LogisticsInfoListResultSetDto.LogisticsSec();
//                sec.setLogisticsAddress("成都【"+i+"】区");
//                sec.setLogisticsTime(System.nanoTime()+"");
//                listLogisticsSec.add(sec);
//            }
//            n.setLogisticsSecList(listLogisticsSec);
//            list.add(n);
//        }
//        System.out.println(com.yatang.xc.xcr.model.ResultMap.successu().setListdata(list).page(1,20, 1, 3L));
//    }

}
