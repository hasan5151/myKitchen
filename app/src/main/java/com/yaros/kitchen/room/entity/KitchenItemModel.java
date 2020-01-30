package com.yaros.kitchen.room.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import retrofit2.http.Path;

@Entity
public class KitchenItemModel {

    @PrimaryKey(autoGenerate = false)
    private Integer id;
    private String number;
    private String order_items;
    private String name;
    private String comment;
    private Long reqTime;
    private String date;
    private Integer count;
    private Integer isCountDownStarted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOrder_items() {
        return order_items;
    }

    public void setOrder_items(String order_items) {
        this.order_items = order_items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getReqTime() {
        return reqTime;
    }

    public void setReqTime(Long reqTime) {
        this.reqTime = reqTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getIsCountDownStarted() {
        return isCountDownStarted;
    }

    public void setIsCountDownStarted(Integer isCountDownStarted) {
        this.isCountDownStarted = isCountDownStarted;
    }


    public KitchenItemModel(String number, String order_items, String name, String comment, Long reqTime, String date, Integer count, Integer isCountDownStarted) {
        this.number = number;
        this.order_items = order_items;
        this.name = name;
        this.comment = comment;
        this.reqTime = reqTime;
        this.date = date;
        this.count = count;
        this.isCountDownStarted = isCountDownStarted;
    }
}
