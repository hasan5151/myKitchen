package com.yaros.kitchen.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class ApiItemModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var order_items : String,
    var dish: String,
    var number : String,
    var count: Int,
    var comment: String?,
    var date: String
)
