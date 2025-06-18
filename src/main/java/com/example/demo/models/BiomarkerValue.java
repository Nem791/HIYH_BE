package com.example.demo.models;

import java.util.Date;

public class BiomarkerValue {
    private Object value;
    private String resultFlag;
    private String referenceRange;
    private String resultUnits;
    private Date timeResulted;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getReferenceRange() {
        return referenceRange;
    }

    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    public String getResultUnits() {
        return resultUnits;
    }

    public void setResultUnits(String resultUnits) {
        this.resultUnits = resultUnits;
    }

    public Date getTimeResulted() {
        return timeResulted;
    }

    public void setTimeResulted(Date timeResulted) {
        this.timeResulted = timeResulted;
    }
}