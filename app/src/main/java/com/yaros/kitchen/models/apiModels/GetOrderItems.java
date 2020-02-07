package com.yaros.kitchen.models.apiModels;

import java.util.List;

public class GetOrderItems {
    private String printer;
    private List<ApiOrderModel> orders;

    public String getPrinter() {
        return printer;
    }

    public List<ApiOrderModel> getOrders() {
        return orders;
    }
}
