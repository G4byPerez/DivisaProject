package com.gabyperez.divisaserver.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Moneda::class], version = 1, exportSchema = false)
abstract class MonedaDB  : RoomDatabase(){
    abstract fun MonedaDAO(): MonedaDAO

    companion object {
        private var INSTANCE: MonedaDB? = null
        fun getDatabase(context: Context, scope: CoroutineScope): MonedaDB {

            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,MonedaDB::class.java, "moneda")
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}