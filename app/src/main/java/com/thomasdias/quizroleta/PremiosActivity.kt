package com.thomasdias.quizroleta

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thomasdias.quizroleta.data.Premio

class PremiosActivity : AppCompatActivity() {

    private lateinit var viewModel: QuizViewModel
    private lateinit var adapter: PremioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premios)

        val etNomePremio = findViewById<EditText>(R.id.etNomePremio)
        val btnAdd = findViewById<Button>(R.id.btnAddPremio)
        val recyclerView = findViewById<RecyclerView>(R.id.rvPremios)

        // 1. Configurar ViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(QuizViewModel::class.java)

        // 2. Configurar RecyclerView (A Lista)
        adapter = PremioAdapter { premioParaDeletar ->
            viewModel.deletarPremio(premioParaDeletar)
            Toast.makeText(this, "Deletado!", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 3. Observar dados do Banco
        viewModel.todosPremios.observe(this) { lista ->
            adapter.atualizarLista(lista)
        }

        // 4. Botão Adicionar
        btnAdd.setOnClickListener {
            val texto = etNomePremio.text.toString()
            if (texto.isNotEmpty()) {
                val novoPremio = Premio(nome = texto)
                viewModel.salvarPremio(novoPremio)
                etNomePremio.text.clear()
            } else {
                Toast.makeText(this, "Digite um nome!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}