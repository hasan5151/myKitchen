package com.yaros.kitchen.repositories

import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel

class ApiRepo (val repos : Repos, val rxSchedulers: RxSchedulers, val apiService: ApiService) {

    fun fetchItems(){
        KitchenItemModel(1,1,"Salat",null,"14:05","12:55",1).let { repos.getItemRepo().insert(it) }
        KitchenItemModel(2,1,"Salat2","ris2","4:25","10:55",2).let {  repos.getItemRepo().insert(it)}
        KitchenItemModel(3,2,"Salat3","ris2","10:25","11:00",6).let { repos.getItemRepo().insert(it) }
    }

    fun fetchOrders(){
        for (x in showKitchenOrders()){
            repos.getOrderRepo().insert(x)
        }
    }

    fun showKitchenOrders(): List<KitchenOrderModel>{
        val order1 = KitchenOrderModel(1,"Айжамал", listOf(1,2))
        val order2 = KitchenOrderModel(2,"Елена", listOf(3))
        return listOf(order1,order2)
    }


}