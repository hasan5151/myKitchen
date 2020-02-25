package com.yaros.kitchen.models.apiModels;

public class DishCookedModel { //to send Server
    private String ticket;
    private String order;
    private String dish;
  //  String comment;
    private Long cooking_date;
    private Long cooking_time;
    private String printer;

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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

/*    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }*/

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

    public DishCookedModel(String ticket, String order, String dish, Long cooking_date, Long cooking_time,String printer) {
        this.ticket = ticket;
        this.order = order;
        this.printer = printer;
        this.dish = dish;
        this.cooking_date = cooking_date;
        this.cooking_time = cooking_time;
    }
}
