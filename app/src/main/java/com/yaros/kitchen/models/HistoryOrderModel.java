package com.yaros.kitchen.models;

import java.util.ArrayList;
import java.util.List;

public class HistoryOrderModel {
    private Integer id;
    private String waiterName;
    private List<HistoryItemModel> orderItemsModelList;

    public HistoryOrderModel(Integer id, String workerName, List<HistoryItemModel> orderItemsModelList) {
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

    public List<HistoryItemModel> getOrderItemsModelList() {
        return orderItemsModelList;
    }

    public void setOrderItemsModelList(ArrayList<HistoryItemModel> orderItemsModelList) {
        this.orderItemsModelList = orderItemsModelList;
    }
}
