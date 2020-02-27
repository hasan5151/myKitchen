package com.yaros.kitchen.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yaros.kitchen.room.converters.Converters
import com.yaros.kitchen.room.dao.*
import com.yaros.kitchen.room.entity.*

@Database(
    entities = [DishesModel::class, WaitersModel::class, PrintersModel::class, KitchenModel::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(Converters::class)
abstract class RoomDb : RoomDatabase(){

    abstract fun DishesDAO(): DishesDAO
    abstract fun WaiterDAO(): WaiterDAO
    abstract fun PrintersDAO(): PrintersDAO
    abstract fun KitchenDAO(): KitchenDAO

    companion object {
        @Volatile private var instance: RoomDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            RoomDb::class.java, "kitchen.db").allowMainThreadQueries()
            .build()
    }
}