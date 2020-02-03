package com.yaros.kitchen.models.apiModels;

public class ItemApiModel {
    private String dish;
    private Integer count;
    private String item_date;
    private String comment="";

    public String getDish() {
        return dish;
    }

    public Integer getCount() {
        return count;
    }

    public String getItem_date() {
        return item_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
