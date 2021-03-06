package com.zczp.vo_yycoder;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class CollectPostDetailVo extends PostDetailVo implements Serializable {

    @ApiModelProperty("招聘表发布状态（-1:已删除 0:待审核 1:已审核 ）")
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
