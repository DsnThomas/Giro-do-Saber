package com.thomasdias.quizroleta.data // Verifique se o pacote é esse mesmo

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity avisa ao Room que isso é uma tabela do banco de dados
@Entity(tableName = "tabela_perguntas")
data class Pergunta(
    // @PrimaryKey define o ID único. autoGenerate = true cria o ID sozinho (1, 2, 3...)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val enunciado: String,      // A pergunta em si
    val opcaoA: String,         // Texto da alternativa A
    val opcaoB: String,         // Texto da alternativa B
    val opcaoC: String,         // Texto da alternativa C
    val opcaoD: String,         // Texto da alternativa D
    val indiceCorreta: Int      // 0, 1, 2 ou 3 (Para sabermos qual é a certa)
)