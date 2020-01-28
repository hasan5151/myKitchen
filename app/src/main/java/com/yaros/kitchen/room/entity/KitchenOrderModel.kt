package com.yaros.kitchen.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.yaros.kitchen.room.converters.Converters
import java.util.ArrayList


@Entity
@TypeConverters(Converters::class)
data class KitchenOrderModel (
    @PrimaryKey(autoGenerate = false)
    val order_item: String,
    val number: String,
    val waiterName: String = ""
)
