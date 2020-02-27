package com.yaros.kitchen.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class KitchenModel {

    @PrimaryKey(autoGenerate = false)
    public Integer id;
    public String number;
    public String printerId;
    public String printerName;
    public String order_item;
    public String name;
    public String comment;
    public String dish;
    public Long reqTime;
    public String date;
    public String dateOriginal;
    public Integer count;
    public String waiterName;
    public Integer countDownStatus;
    public Integer isSent;
    public Integer cancelledOrders;
    public String item_order;
    public String item_number;

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

    public String getItem_order() {
        return item_order;
    }

    public void setItem_order(String item_order) {
        this.item_order = item_order;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public KitchenModel(String number, String printerId, String order_item, String name,
                        String comment, String dish, Long reqTime, String date, String dateOriginal, Integer count, String waiterName, Integer countDownStatus, String printerName,
                        Integer isSent, Integer cancelledOrders, String item_order, String item_number) {
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
        this.item_number= item_number;
        this.item_order=item_order;
    }
}
