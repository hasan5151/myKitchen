package com.yaros.kitchen.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class HashModel {
    private String orders_hash;
    private String catalog_hash;
    private String time_out;
    private Long time_server;

    public String getCatalog_hash() {
        return catalog_hash;
    }

    public void setCatalog_hash(String catalog_hash) {
        this.catalog_hash = catalog_hash;
    }

    public String getOrders_hash() {
        return orders_hash;
    }

    public void setOrders_hash(String orders_hash) {
        this.orders_hash = orders_hash;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public Long getTime_server() {
        return time_server;
    }

    public void setTime_server(Long time_server) {
        this.time_server = time_server;
    }
}
