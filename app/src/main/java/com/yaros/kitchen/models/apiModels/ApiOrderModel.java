package com.yaros.kitchen.models.apiModels;

import java.util.List;

public class ApiOrderModel {
    private String order;
    private Long date;
    private String number;
    private String waiter;
    private List<ItemApiModel> dishes;

    public String getOrder() {
        return order;
    }

    public Long getDate() {
        return date;
    }

    public String getNumber() {
        return number;
    }

    public String getWaiter() {
        return waiter;
    }

    public List<ItemApiModel> getDishes() {
        return dishes;
    }
}
