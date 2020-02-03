package com.yaros.kitchen.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WaitersModel (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val deleted: Boolean=false
)