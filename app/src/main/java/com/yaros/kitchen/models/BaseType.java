package com.yaros.kitchen.models;

import com.yaros.kitchen.room.entity.WaitersModel;
import com.yaros.kitchen.room.entity.ApiItemModel;
import com.yaros.kitchen.room.entity.DishesModel;

import java.util.List;

public class BaseType {
    private List<PrintersModel> printers;
    private List<DishesModel> dishes;
    private List<ApiItemModel> orders;
    private List<WaitersModel> waiters;

    public List<PrintersModel> getPrinters() {
        return printers;
    }

    public void setPrinters(List<PrintersModel> printers) {
        this.printers = printers;
    }

    public List<DishesModel> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishesModel> dishes) {
        this.dishes = dishes;
    }

    public List<ApiItemModel> getOrders() {
        return orders;
    }

    public void setOrders(List<ApiItemModel> orders) {
        this.orders = orders;
    }

    public List<WaitersModel> getWaiters() {
        return waiters;
    }

    public void setWaiters(List<WaitersModel> waiters) {
        this.waiters = waiters;
    }
}
