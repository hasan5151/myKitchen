package com.yaros.kitchen.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity()
data class DishesModel(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String,
    var cookingTime: Long
)