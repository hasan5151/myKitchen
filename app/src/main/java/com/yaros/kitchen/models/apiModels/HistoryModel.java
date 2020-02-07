package com.yaros.kitchen.models.apiModels;

public class HistoryModel {
    private String order;
    private String printer;
    private String ticket;
    private String dish;
    private Long cooking_date;
    private Long cooking_time;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public Long getCooking_date() {
        return cooking_date;
    }

    public void setCooking_date(Long cooking_date) {
        this.cooking_date = cooking_date;
    }

    public Long getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(Long cooking_time) {
        this.cooking_time = cooking_time;
    }

    public HistoryModel(String order, String printer, String ticket, String dish, Long cooking_date, Long cooking_time) {
        this.order = order;
        this.printer = printer;
        this.ticket = ticket;
        this.dish = dish;
        this.cooking_date = cooking_date;
        this.cooking_time = cooking_time;
    }

}

