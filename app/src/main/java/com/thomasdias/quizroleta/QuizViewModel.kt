package com.thomasdias.quizroleta

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.thomasdias.quizroleta.data.AppDatabase
import com.thomasdias.quizroleta.data.Pergunta
import com.thomasdias.quizroleta.data.Premio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    // --- ESTA PARTE É OBRIGATÓRIA PARA FUNCIONAR ---
    private val perguntaDao = AppDatabase.getDatabase(application).perguntaDao()
    private val premioDao = AppDatabase.getDatabase(application).premioDao()
    // ----------------------------------------------

    // Listas observáveis (LiveData)
    val todasPerguntas: LiveData<List<Pergunta>> = perguntaDao.lerTodasPerguntas()
    val todosPremios: LiveData<List<Premio>> = premioDao.lerPremios()

    // --- FUNÇÕES DE PERGUNTAS ---
    fun salvarPergunta(pergunta: Pergunta) {
        viewModelScope.launch(Dispatchers.IO) {
            perguntaDao.adicionarPergunta(pergunta)
        }
    }

    fun deletarPergunta(pergunta: Pergunta) {
        viewModelScope.launch(Dispatchers.IO) {
            perguntaDao.deletarPergunta(pergunta)
        }
    }

    // --- FUNÇÕES DE PRÊMIOS ---
    fun salvarPremio(premio: Premio) {
        viewModelScope.launch(Dispatchers.IO) {
            premioDao.adicionarPremio(premio)
        }
    }

    fun deletarPremio(premio: Premio) {
        viewModelScope.launch(Dispatchers.IO) {
            premioDao.deletarPremio(premio)
        }
    }
}