package com.yaros.kitchen.models;

public class HashModel {

    private String catalog_hash;
    private String order_hash;
    private String time_out;
    private String time_server;

    public String getCatalog_hash() {
        return catalog_hash;
    }

    public void setCatalog_hash(String catalog_hash) {
        this.catalog_hash = catalog_hash;
    }

    public String getOrder_hash() {
        return order_hash;
    }

    public void setOrder_hash(String order_hash) {
        this.order_hash = order_hash;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public String getTime_server() {
        return time_server;
    }

    public void setTime_server(String time_server) {
        this.time_server = time_server;
    }
}
