package com.yaros.kitchen.repositories

import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb

class Repos (val db : RoomDb,val rxSchedulers: RxSchedulers){
    fun getItemRepo()= ItemRepo(db,rxSchedulers)
    fun getOrderRepo()= OrderRepo(db,rxSchedulers)
    fun getApiItemRepo()= ApiItemRepo(db,rxSchedulers)
    fun getDishesRepo()= DishesRepo(db,rxSchedulers)
    fun getWaiterRepo()= WaiterRepo(db,rxSchedulers)
    fun getPrintersRepo()= PrintersRepo(db,rxSchedulers)
}