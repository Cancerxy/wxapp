package com.zczp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TbPostType {
    private Integer typeId;

    private String typeName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date typeNewDate;

    private Integer typeState;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Date getTypeNewDate() {
        return typeNewDate;
    }

    public void setTypeNewDate(Date typeNewDate) {
        this.typeNewDate = typeNewDate;
    }

    public Integer getTypeState() {
        return typeState;
    }

    public void setTypeState(Integer typeState) {
        this.typeState = typeState;
    }
}