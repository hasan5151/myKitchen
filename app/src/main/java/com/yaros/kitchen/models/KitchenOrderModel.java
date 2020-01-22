package com.yaros.kitchen.models;

import java.util.ArrayList;

public class KitchenOrderModel {
    private Integer id;
    private String waiterName;
    private ArrayList<KitchenItemModel> orderItemsModelList;

    public KitchenOrderModel(Integer id, String workerName, ArrayList<KitchenItemModel> orderItemsModelList) {
        this.id = id;
        this.waiterName = workerName;
        this.orderItemsModelList = orderItemsModelList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

     public ArrayList<KitchenItemModel> getOrderItemsModelList() {
        return orderItemsModelList;
    }

    public void setOrderItemsModelList(ArrayList<KitchenItemModel> orderItemsModelList) {
        this.orderItemsModelList = orderItemsModelList;
    }
}
