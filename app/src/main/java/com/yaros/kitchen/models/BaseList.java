package com.yaros.kitchen.models;

import java.util.List;

public class  BaseList<T> {
    private T data;
    private Meta meta;

    public T getData() {
        return data;
    }


    public Meta getMeta() {
        return meta;
    }
}
