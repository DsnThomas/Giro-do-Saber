package com.thomasdias.quizroleta.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_premios")
data class Premio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String
)