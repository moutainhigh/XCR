package com.yatang.xc.xcr.biz.message.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 分页扫码流水返回
 * Created by wangyang on 2017/10/17.
 */
public class SwiftPassReturnQuery implements Serializable {

    private static final long serialVersionUID = 8827274945913079331L;
    private long totalCount;
    private List<SwiftPassReturnDto> swiftPassReturnDtoList;

    public SwiftPassReturnQuery() {
    }

    public SwiftPassReturnQuery(long totalCount, List<SwiftPassReturnDto> swiftPassReturnDtoList) {
        this.totalCount = totalCount;
        this.swiftPassReturnDtoList = swiftPassReturnDtoList;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<SwiftPassReturnDto> getSwiftPassReturnDtoList() {
        return swiftPassReturnDtoList;
    }

    public void setSwiftPassReturnDtoList(List<SwiftPassReturnDto> swiftPassReturnDtoList) {
        this.swiftPassReturnDtoList = swiftPassReturnDtoList;
    }
}
