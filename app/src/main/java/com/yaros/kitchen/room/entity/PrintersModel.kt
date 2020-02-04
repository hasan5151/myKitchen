package com.yaros.kitchen.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data  class PrintersModel(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String,
    var isChecked : Boolean = false
)