package com.example.eva2.listadecompras.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eva2.listadecompras.dao.CompraDAO
import com.example.eva2.listadecompras.entities.Compra

@Database(entities = [Compra::class], version = 1)
abstract class DatabaseConnection : RoomDatabase() {
    abstract fun compraDAO(): CompraDAO

    companion object {

        @Volatile
        private var BASE_DATOS: DatabaseConnection? = null
        fun getInstance(contexto: Context): DatabaseConnection {
            // synchronized previene el acceso de múltiples threads de manera simultánea

            return BASE_DATOS ?: synchronized(this) {
                Room.databaseBuilder(
                    contexto.applicationContext,
                    DatabaseConnection::class.java,
                    "ComprasBD.bd"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { BASE_DATOS = it }
            }
        }
    }
}