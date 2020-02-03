package com.yaros.kitchen.models.apiModels;

import java.util.List;

public class OrdersKitchenPostModel {
    private List<String> printers;
    private Long date_begin;
    private Long date_end;

    public List<String> getPrinters() {
        return printers;
    }

    public Long getDate_begin() {
        return date_begin;
    }

    public Long getDate_end() {
        return date_end;
    }

    public OrdersKitchenPostModel(List<String> printers, Long date_begin, Long date_end) {
        this.printers = printers;
        this.date_begin = date_begin;
        this.date_end = date_end;
    }
}
