package com.yaros.kitchen.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class KitchenModel {



    @PrimaryKey(autoGenerate = false)
    private Integer id;
    private String number;
    private String printerId;
    private String printerName;
    private String order_item;
    private String name;
    private String comment;
    private String dish;
    private Long reqTime;
    private String date;
    private String dateOriginal;
    private Integer count;
    private String waiterName;
    private Integer countDownStatus;
    private Integer isSent= 0;
    private Integer cancelledOrders = 0;


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

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    public String getOrder_item() {
        return order_item;
    }

    public void setOrder_item(String order_item) {
        this.order_item = order_item;
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

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
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

    public Integer getCountDownStatus() {
        return countDownStatus;
    }

    public void setCountDownStatus(Integer countDownStatus) {
        this.countDownStatus = countDownStatus;
    }

    public String getDateOriginal() {
        return dateOriginal;
    }

    public void setDateOriginal(String dateOriginal) {
        this.dateOriginal = dateOriginal;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getPrinterName() {
        return printerName;
    }

    public Integer getIsSent() {
        return isSent;
    }

    public void setIsSent(Integer isSent) {
        this.isSent = isSent;
    }

    public Integer getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(Integer cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public KitchenModel(String number, String printerId, String order_item, String name,
                        String comment, String dish, Long reqTime, String date, String dateOriginal, Integer count, String waiterName, Integer countDownStatus, String printerName,
                        Integer isSent,Integer cancelledOrders) {
        this.number = number;
        this.printerId = printerId;
        this.printerName = printerName;
        this.order_item = order_item;
        this.name = name;
        this.comment = comment;
        this.dish = dish;
        this.reqTime = reqTime;
        this.date = date;
        this.dateOriginal = dateOriginal;
        this.count = count;
        this.waiterName = waiterName;
        this.countDownStatus = countDownStatus;
        this.isSent= isSent;
        this.cancelledOrders= cancelledOrders;
    }
}
