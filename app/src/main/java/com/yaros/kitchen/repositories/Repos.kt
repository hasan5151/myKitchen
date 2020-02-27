package com.yaros.kitchen.repositories

import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb

class Repos (val db : RoomDb,val rxSchedulers: RxSchedulers){
    fun getDishesRepo()= DishesRepo(db,rxSchedulers)
    fun getWaiterRepo()= WaiterRepo(db,rxSchedulers)
    fun getPrintersRepo()= PrintersRepo(db,rxSchedulers)
    fun getKitchenRepo()= KitchenRepo(db,rxSchedulers)
}