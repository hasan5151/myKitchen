package com.yaros.kitchen.repositories

import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSingleSchedulers
 import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel

class ApiRepo (val repos : Repos, val rxSingleSchedulers: RxSingleSchedulers, val apiService: ApiService) {

    fun fetchItems(){}


    fun fetchOrders(){
        for (x in showKitchenOrders()){
            repos.getOrderRepo().insert(x)
        }
    }


    fun showKitchenOrders(): List<KitchenOrderModel>{

        val test = KitchenItemModel(1,"Salat","ris","14:25","11:00",1)
        val test2 = KitchenItemModel(2,"Salat2","ris2","14:25","11:00",2)

        val test3 = KitchenItemModel(3,"Salat3","ris2","14:25","11:00",6)

        val order1 = KitchenOrderModel(1,"Айжамал", listOf(1,2))
        val order2 = KitchenOrderModel(2,"Елена", listOf(3))
        return listOf(order1,order2)


     /*   val item1 = KitchenItemModel(
            "1",
            "Salat",
            "ris",
            "14:25",
            "11:00",
            1
        )
        val order1 = KitchenOrderModel(1,"Айжамал", arrayListOf(item1))

        val item2 = KitchenItemModel(
            "2",
            "Salat2",
            "",
            "04:05",
            "11:20",
            1
        )
        val item3 = KitchenItemModel(
            "3",
            "Salat3",
            "ris3", //
            "21:25",
            "11:18",
            2
        )
        val item4 = KitchenItemModel(
            "4",
            "Salat4",
            "ris3", //
            "17:25",
            "11:18",
            5
        )

        val order2 = KitchenOrderModel(2,"Елена", arrayListOf(item2,item3,item4))
        return arrayListOf(order1,order2)*/
    }


}