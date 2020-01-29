package com.yaros.kitchen.repositories

import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.entity.*

class ApiRepo (val repos : Repos, val rxSchedulers: RxSchedulers, val apiService: ApiService) {

    fun fetchItems(){
        System.out.println("size of ananin ami")
        DishesModel("1","Fish",12).let {
            repos.getDishesRepo().insert(it)
        }
        DishesModel("2","Cola",10).let {
            repos.getDishesRepo().insert(it)
        }

        ApiItemModel(1,"1","1","001",3,"test",System.currentTimeMillis().toString(),"1").let {
            repos.getApiItemRepo().insert(it)
        }
        ApiItemModel(2,"1","2","001",2,"test",System.currentTimeMillis().toString(),"1").let {
            repos.getApiItemRepo().insert(it)
        }
        ApiItemModel(3,"2","2","002",1,null,System.currentTimeMillis().toString(),"1").let {
            repos.getApiItemRepo().insert(it)
        }
        ApiItemModel(4,"2","1","002",5,null,System.currentTimeMillis().toString(),"1").let {
            repos.getApiItemRepo().insert(it)
        }


    }

    fun fetchOrders(){
  /*      for (x in showKitchenOrders()){
            repos.getOrderRepo().insert(x)
        }*/
    }

    fun fetchDishes(){

     }

    fun fetchPrinters(){

    }

    fun fetchItemsWithTimeStamp(printerId: String, startDate: Long, endData: Long){

    }


}