package com.yaros.kitchen.models;

public class Base<T> {
    private T data;
    private Meta meta;

    public T getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
