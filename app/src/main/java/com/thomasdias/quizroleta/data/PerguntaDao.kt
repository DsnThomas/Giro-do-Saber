package com.thomasdias.quizroleta.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PerguntaDao {

    // O ViewModel procura exatamente por este nome: "adicionarPergunta"
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun adicionarPergunta(pergunta: Pergunta)

    // O ViewModel procura por "deletarPergunta"
    @Delete
    suspend fun deletarPergunta(pergunta: Pergunta)

    // O ViewModel procura exatamente por este nome: "lerTodasPerguntas"
    @Query("SELECT * FROM tabela_perguntas ORDER BY id ASC")
    fun lerTodasPerguntas(): LiveData<List<Pergunta>>

}