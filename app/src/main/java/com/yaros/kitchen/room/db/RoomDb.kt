package com.yaros.kitchen.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yaros.kitchen.room.converters.Converters
import com.yaros.kitchen.room.dao.KitchenItemDAO
import com.yaros.kitchen.room.dao.KitchenOrderDAO
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel

@Database(
    entities = [KitchenItemModel::class, KitchenOrderModel::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(Converters::class)
abstract class RoomDb : RoomDatabase(){
    abstract fun KitchenOrderDAO(): KitchenOrderDAO
    abstract fun KitchenItemDAO(): KitchenItemDAO

    companion object {
        @Volatile private var instance: RoomDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            RoomDb::class.java, "kitchen.db")
            .build()
    }
}