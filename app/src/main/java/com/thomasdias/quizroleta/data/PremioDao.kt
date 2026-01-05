package com.thomasdias.quizroleta.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PremioDao {
    @Query("SELECT * FROM tabela_premios")
    fun lerPremios(): LiveData<List<Premio>>

    @Insert
    fun adicionarPremio(premio: Premio)

    @Delete
    fun deletarPremio(premio: Premio)
}