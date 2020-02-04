package com.yaros.kitchen.models;

public class HistoryItemModel {
    private String title;
    private String subTitle;
    private String reqTime;
    private String orderTime;
    private Boolean isCookedOnTime;


    public HistoryItemModel(String title, String reqTime, String orderTime, Boolean isCookedOnTime) {
        this.title = title;
        this.reqTime = reqTime;
        this.orderTime = orderTime;
        this.isCookedOnTime = isCookedOnTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Boolean getCookedOnTime() {
        return isCookedOnTime;
    }

    public void setCookedOnTime(Boolean cookedOnTime) {
        isCookedOnTime = cookedOnTime;
    }
}

