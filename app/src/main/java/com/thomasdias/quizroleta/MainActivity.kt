package com.thomasdias.quizroleta

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.thomasdias.quizroleta.data.Pergunta
import com.google.android.material.card.MaterialCardView
import com.thomasdias.quizroleta.data.Premio

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: QuizViewModel

    // Lista e Controle
    private var listaPerguntas: List<Pergunta> = mutableListOf()
    private var indiceAtual = 0
    private var pontuacao = 0

    // Componentes da Tela
    private lateinit var tvPergunta: TextView
    private lateinit var tvPontuacao: TextView
    private lateinit var tvFeedback: TextView // O texto que diz ACERTOU/ERROU
    private lateinit var cardPergunta: MaterialCardView // O cartão branco

    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Ligar as variáveis aos IDs da tela
        tvPergunta = findViewById(R.id.tvPergunta)
        tvPontuacao = findViewById(R.id.tvPontuacao)
        tvFeedback = findViewById(R.id.tvFeedback)
        cardPergunta = findViewById(R.id.cardPergunta)

        btnA = findViewById(R.id.btnOpcaoA)
        btnB = findViewById(R.id.btnOpcaoB)
        btnC = findViewById(R.id.btnOpcaoC)
        btnD = findViewById(R.id.btnOpcaoD)

        // 2. ViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(QuizViewModel::class.java)

        // 3. Observar banco de dados
        viewModel.todasPerguntas.observe(this) { lista ->
            if (lista.isEmpty()) {
                criarPerguntasDeTeste()
            } else {
                // --- NOVA LÓGICA AQUI ---

                // 1. Ler a configuração (Padrão = 5 perguntas se não tiver nada salvo)
                val prefs = getSharedPreferences("QUIZ_CONFIG", MODE_PRIVATE)
                val qtdDesejada = prefs.getInt("QTD_PERGUNTAS", 5)

                // 2. Embaralhar e pegar só a quantidade desejada
                listaPerguntas = lista.shuffled().take(qtdDesejada)

                // Reiniciar índices
                indiceAtual = 0
                pontuacao = 0
                tvPontuacao.text = "Pontos: 0"

                mostrarPerguntaAtual()
            }
        }

        viewModel.todosPremios.observe(this) { listaPremios ->
            if (listaPremios.isEmpty()) {
                criarPremiosDeTeste()
            }
        }

        // 4. Cliques
        btnA.setOnClickListener { conferirResposta(0, btnA) }
        btnB.setOnClickListener { conferirResposta(1, btnB) }
        btnC.setOnClickListener { conferirResposta(2, btnC) }
        btnD.setOnClickListener { conferirResposta(3, btnD) }
    }

    private fun mostrarPerguntaAtual() {
        // Resetar visual (Cor dos botões volta ao branco, esconde feedback)
        resetarVisual()

        if (indiceAtual < listaPerguntas.size) {
            val p = listaPerguntas[indiceAtual]
            tvPergunta.text = p.enunciado
            btnA.text = p.opcaoA
            btnB.text = p.opcaoB
            btnC.text = p.opcaoC
            btnD.text = p.opcaoD
        } else {
            // Fim de jogo
            tvPergunta.text = "Fim de Jogo!"

            // Vamos esperar 1 segundo e abrir a Roleta
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, RoletaActivity::class.java)
                // Opcional: Passar a pontuação para a roleta
                intent.putExtra("PONTUACAO_FINAL", pontuacao)
                startActivity(intent)
                finish() // Fecha o Quiz para não voltar
            }, 1000)
        }
    }

    private fun conferirResposta(indiceEscolhido: Int, botaoClicado: Button) {
        if (indiceAtual >= listaPerguntas.size) return

        // Bloquear cliques para não bugar
        desabilitarBotoes()

        val perguntaCerta = listaPerguntas[indiceAtual]
        val acertou = (indiceEscolhido == perguntaCerta.indiceCorreta)

        // EFEITOS VISUAIS
        tvPergunta.visibility = View.INVISIBLE // Esconde a pergunta
        tvFeedback.visibility = View.VISIBLE   // Mostra o Feedback

        if (acertou) {
            pontuacao += 10
            tvPontuacao.text = "$pontuacao"

            // Texto Verde e Botão Verde
            tvFeedback.text = "ACERTOU!"
            tvFeedback.setTextColor(ContextCompat.getColor(this, R.color.quiz_green))
            botaoClicado.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.quiz_green))
        } else {
            // Texto Vermelho e Botão Vermelho
            tvFeedback.text = "ERROU!"
            tvFeedback.setTextColor(ContextCompat.getColor(this, R.color.quiz_red))
            botaoClicado.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.quiz_red))
        }

        // Esperar 2 segundos e ir para a próxima
        Handler(Looper.getMainLooper()).postDelayed({
            indiceAtual++
            mostrarPerguntaAtual()
        }, 2000) // 2000 milissegundos = 2 segundos
    }

    private fun resetarVisual() {
        // Volta botões para Branco
        val corBranca = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.quiz_white))
        btnA.backgroundTintList = corBranca
        btnB.backgroundTintList = corBranca
        btnC.backgroundTintList = corBranca
        btnD.backgroundTintList = corBranca

        // Reativa botões
        btnA.isEnabled = true
        btnB.isEnabled = true
        btnC.isEnabled = true
        btnD.isEnabled = true

        // Volta texto normal
        tvFeedback.visibility = View.GONE
        tvPergunta.visibility = View.VISIBLE
    }

    private fun desabilitarBotoes() {
        btnA.isEnabled = false
        btnB.isEnabled = false
        btnC.isEnabled = false
        btnD.isEnabled = false
    }

    private fun criarPerguntasDeTeste() {
        Thread {
            // Lista OFICIAL de perguntas que vai para a Play Store
            val perguntasIniciais = listOf(
                // SICOOB & COOPERATIVISMO
                Pergunta(0, "Qual é o principal objetivo de uma cooperativa de crédito?", "Gerar lucro máximo", "Prestar serviços vantajosos", "Vender eletrônicos", "Competir com bancos", 1),
                Pergunta(0, "Como são chamados os 'donos' de uma cooperativa?", "Clientes", "Acionistas", "Cooperados", "Funcionários", 2),
                Pergunta(0, "O que acontece com as sobras (lucro) da cooperativa?", "Ficam pro governo", "São distribuídas aos sócios", "Doadas para bancos", "Desaparecem", 1),
                Pergunta(0, "Qual destes é um princípio do cooperativismo?", "Gestão democrática", "Lucro acima de tudo", "Exclusividade para ricos", "Centralização de poder", 0),
                Pergunta(0, "O que significa a sigla SICOOB?", "Sistema de Cobrança", "Sistema de Cooperativas do Brasil", "Sociedade Interna", "Sindicato Bancário", 1),

                // EDUCAÇÃO FINANCEIRA
                Pergunta(0, "O que são Juros Compostos?", "Taxa única", "Juros sobre juros", "Taxa do governo", "Desconto à vista", 1),
                Pergunta(0, "Qual a recomendação para Reserva de Emergência?", "Comprar roupa", "6 a 12 meses de custo de vida", "Investir em ações", "Gastar tudo", 1),
                Pergunta(0, "O que é o PIX?", "Imposto", "Pagamento instantâneo", "Marca de cartão", "Investimento", 1),
                Pergunta(0, "Se você gasta mais do que ganha, você está:", "Poupando", "Investindo", "Endividado", "Lucrando", 2),

                // GERAIS
                Pergunta(0, "Qual é o maior planeta do Sistema Solar?", "Terra", "Marte", "Júpiter", "Saturno", 2),
                Pergunta(0, "Quem pintou a Mona Lisa?", "Van Gogh", "Da Vinci", "Picasso", "Michelangelo", 1),
                Pergunta(0, "Quantos estados tem o Brasil?", "24", "26 + DF", "27 + DF", "30", 1),
                Pergunta(0, "Elemento químico representado pela letra O?", "Ouro", "Oxigênio", "Ósmio", "Prata", 1),
                Pergunta(0, "Ano que o homem pisou na Lua?", "1950", "1969", "1980", "2000", 1),
                Pergunta(0, "Qual é a capital da França?", "Londres", "Berlim", "Madri", "Paris", 3)
            )

            // Insere uma por uma no banco
            for (p in perguntasIniciais) {
                viewModel.salvarPergunta(p)
            }
        }.start()
    }
    private fun criarPremiosDeTeste() {
        Thread {
            val premiosIniciais = listOf(
                Premio(nome = "Caneta"),
                Premio(nome = "Boné"),
                Premio(nome = "50 Reais"),
                Premio(nome = "Chaveiro"),
                Premio(nome = "Tente Novamente"), // Importante ter um item "neutro"
                Premio(nome = "Garrafa")
            )

            for (p in premiosIniciais) {
                viewModel.salvarPremio(p)
            }
        }.start()
    }

}