package com.yaros.kitchen.models;

public class KitchenItemModel {
    private String title;
    private String subTitle;
    private String reqTime;
    private String orderTime;
    private Integer badge;


    public KitchenItemModel(String title, String subTitle, String reqTime, String orderTime, Integer badge) {
        this.title = title;
        this.subTitle = subTitle;
        this.reqTime = reqTime;
        this.orderTime = orderTime;
        this.badge = badge;
    }

    public Integer getBadge() {
        return badge;
    }

    public void setBadge(Integer badge) {
        this.badge = badge;
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
}
