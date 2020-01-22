package com.yaros.kitchen.models;

import java.util.List;

public class BaseList<T> {
    private List<T> data;
    private Meta meta;

    public List<T> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }
}
