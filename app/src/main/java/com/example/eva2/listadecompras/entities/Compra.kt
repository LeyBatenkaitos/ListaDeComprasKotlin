package com.example.eva2.listadecompras.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Compra (
    @PrimaryKey(autoGenerate = true) val id:Int? = null,

    var compraName:String,

    var isCompraRealizada:Boolean
)