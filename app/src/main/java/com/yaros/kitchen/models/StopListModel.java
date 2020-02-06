package com.yaros.kitchen.models;

public class StopListModel {
    private String name;
    private String type;
    private Boolean isStop;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isStop() {
        return isStop;
    }

    public void setStop(Boolean stop) {
        isStop = stop;
    }

    public StopListModel(String name, String type, Boolean isStop) {
        this.name = name;
        this.type = type;
        this.isStop = isStop;
    }
}
