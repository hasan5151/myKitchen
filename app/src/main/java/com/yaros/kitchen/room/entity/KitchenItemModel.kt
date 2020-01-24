package com.yaros.kitchen.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class KitchenItemModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var subTitle: String?,
    var reqTime: String,
    var orderTime: String,
    var badge: Int
)