package com.thomasdias.quizroleta

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thomasdias.quizroleta.data.Pergunta

class PerguntasActivity : AppCompatActivity() {

    private lateinit var viewModel: QuizViewModel
    private lateinit var adapter: PerguntaAdapter // <--- Adaptador Novo

    // UI Formulário
    private lateinit var etEnunciado: EditText
    private lateinit var etA: EditText
    private lateinit var etB: EditText
    private lateinit var etC: EditText
    private lateinit var etD: EditText
    private lateinit var rgCorreta: RadioGroup
    private lateinit var btnSalvar: Button

    // UI Config
    private lateinit var etQtdPerguntas: EditText
    private lateinit var btnSalvarConfig: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perguntas)

        // 1. ViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(QuizViewModel::class.java)

        // 2. Inicializar Componentes
        etEnunciado = findViewById(R.id.etEnunciado)
        etA = findViewById(R.id.etOpcaoA)
        etB = findViewById(R.id.etOpcaoB)
        etC = findViewById(R.id.etOpcaoC)
        etD = findViewById(R.id.etOpcaoD)
        rgCorreta = findViewById(R.id.rgCorreta)
        btnSalvar = findViewById(R.id.btnSalvar)

        etQtdPerguntas = findViewById(R.id.etQtdPerguntas)
        btnSalvarConfig = findViewById(R.id.btnSalvarConfig)

        // 3. Configurar a Lista (RecyclerView)
        val rvPerguntas = findViewById<RecyclerView>(R.id.rvPerguntas)
        adapter = PerguntaAdapter { perguntaParaDeletar ->
            // Ação ao clicar na lixeira
            viewModel.deletarPergunta(perguntaParaDeletar)
            Toast.makeText(this, "Pergunta excluída!", Toast.LENGTH_SHORT).show()
        }
        rvPerguntas.adapter = adapter
        rvPerguntas.layoutManager = LinearLayoutManager(this)

        // 4. Observar o Banco de Dados
        viewModel.todasPerguntas.observe(this) { lista ->
            adapter.atualizarLista(lista) // Atualiza a lista na tela sempre que mudar no banco
        }

        // 5. Configuração do Jogo (Salvar qtd)
        val prefs = getSharedPreferences("QUIZ_CONFIG", MODE_PRIVATE)
        val salvo = prefs.getInt("QTD_PERGUNTAS", 5)
        etQtdPerguntas.setText(salvo.toString())

        btnSalvarConfig.setOnClickListener {
            val novoValor = etQtdPerguntas.text.toString().toIntOrNull() ?: 5
            prefs.edit().putInt("QTD_PERGUNTAS", novoValor).apply()
            Toast.makeText(this, "Configuração Salva!", Toast.LENGTH_SHORT).show()
        }

        // 6. Botão Salvar Nova Pergunta
        btnSalvar.setOnClickListener {
            salvarPergunta()
        }
    }

    private fun salvarPergunta() {
        val enunciado = etEnunciado.text.toString()
        val opA = etA.text.toString()
        val opB = etB.text.toString()
        val opC = etC.text.toString()
        val opD = etD.text.toString()

        if (enunciado.isEmpty() || opA.isEmpty() || opB.isEmpty() || opC.isEmpty() || opD.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val indiceCorreto = when (rgCorreta.checkedRadioButtonId) {
            R.id.rbA -> 0
            R.id.rbB -> 1
            R.id.rbC -> 2
            R.id.rbD -> 3
            else -> -1
        }

        if (indiceCorreto == -1) {
            Toast.makeText(this, "Selecione a resposta correta!", Toast.LENGTH_SHORT).show()
            return
        }

        val novaPergunta = Pergunta(
            enunciado = enunciado,
            opcaoA = opA,
            opcaoB = opB,
            opcaoC = opC,
            opcaoD = opD,
            indiceCorreta = indiceCorreto
        )

        viewModel.salvarPergunta(novaPergunta)
        Toast.makeText(this, "Pergunta Salva!", Toast.LENGTH_SHORT).show()
        limparCampos()
    }

    private fun limparCampos() {
        etEnunciado.text?.clear()
        etA.text?.clear()
        etB.text?.clear()
        etC.text?.clear()
        etD.text?.clear()
        rgCorreta.clearCheck()
        etEnunciado.requestFocus()
    }
}