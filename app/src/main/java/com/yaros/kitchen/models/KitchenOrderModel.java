package com.yaros.kitchen.models;

import java.util.ArrayList;
import java.util.List;

public class KitchenOrderModel {
    private Integer id;
    private String workerName;
    private ArrayList<KitchenItemModel> orderItemsModelList;

    public KitchenOrderModel(Integer id, String workerName, ArrayList<KitchenItemModel> orderItemsModelList) {
        this.id = id;
        this.workerName = workerName;
        this.orderItemsModelList = orderItemsModelList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

     public ArrayList<KitchenItemModel> getOrderItemsModelList() {
        return orderItemsModelList;
    }

    public void setOrderItemsModelList(ArrayList<KitchenItemModel> orderItemsModelList) {
        this.orderItemsModelList = orderItemsModelList;
    }
}
