package com.yaros.kitchen.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class KitchenItemModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var number : String,
    var order_items : String,
    var name: String,
    var comment: String?,
    var reqTime: Long,
    var date: String,
    var count: Int,
    var isCountDownStarted : Int =0
)
