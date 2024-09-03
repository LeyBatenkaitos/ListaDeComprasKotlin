package com.example.eva2.listadecompras.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.eva2.listadecompras.entities.Compra

@Dao
interface CompraDAO {
    @Query("SELECT * FROM compra ORDER BY isCompraRealizada ASC")
    suspend fun getAll(): List<Compra>

    @Insert
    suspend fun insertCompra(compra: Compra): Long

    @Update
    suspend fun updateCompra(compra: Compra)

    @Delete
    suspend fun deleteCompra(compra: Compra)
}