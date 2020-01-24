package com.yaros.kitchen.repositories

import com.yaros.kitchen.room.db.RoomDb

class Repos (val db : RoomDb){
    fun getItemRepo()= ItemRepo(db)
    fun getOrderRepo()= OrderRepo(db)
}