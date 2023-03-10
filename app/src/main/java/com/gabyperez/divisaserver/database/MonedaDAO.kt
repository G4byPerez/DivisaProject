package com.gabyperez.divisaserver.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MonedaDAO {
    @Insert
    fun insert(moneda: Moneda)

    @Update
    fun update(moneda : Moneda)

    @Query("SELECT * FROM Moneda WHERE code = :code")
    fun getByCode(code: String) : Moneda

    @Query("SELECT * FROM Moneda WHERE code = :code")
    fun getByCodeCursor(code: String): Cursor

    @Query("SELECT * FROM Moneda")
    fun getAllCursor(): Cursor

    @Query("SELECT * FROM Moneda")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<Moneda>>

    @Query("SELECT * FROM Moneda")
    fun getAllLista(): List<Moneda>

    @Query("DELETE FROM Moneda")
    fun deleteAll()
}