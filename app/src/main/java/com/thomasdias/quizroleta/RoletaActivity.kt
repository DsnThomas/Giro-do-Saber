package com.thomasdias.quizroleta

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlin.random.Random

class RoletaActivity : AppCompatActivity() {

    private lateinit var roletaView: RoletaView
    private lateinit var btnGirar: Button
    private lateinit var btnVoltarMenu: Button
    private lateinit var tvResultado: TextView
    private lateinit var viewModel: QuizViewModel // <--- ViewModel para ler o banco

    // Agora a lista não é fixa, ela começa vazia e muda quando o banco carrega
    private var listaPremios: List<String> = listOf("Carregando...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roleta)

        roletaView = findViewById(R.id.viewRoleta)
        btnGirar = findViewById(R.id.btnGirar)
        btnVoltarMenu = findViewById(R.id.btnVoltarMenu)
        tvResultado = findViewById(R.id.tvResultado)

        // 1. Configurar ViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(QuizViewModel::class.java)

        // 2. Observar o Banco de Dados (Essa é a mágica!)
        viewModel.todosPremios.observe(this) { listaDoBanco ->
            if (listaDoBanco.isNotEmpty()) {
                // Transforma a lista do banco (Objetos) em lista de texto (Strings)
                listaPremios = listaDoBanco.map { it.nome }
                roletaView.definirItens(listaPremios) // Atualiza o desenho
            } else {
                // Se não tiver nada cadastrado, mostra um aviso na roleta
                listaPremios = listOf("Cadastre", "Prêmios", "No", "Menu")
                roletaView.definirItens(listaPremios)
                btnGirar.isEnabled = false // Trava o botão
                Toast.makeText(this, "Cadastre prêmios no menu!", Toast.LENGTH_LONG).show()
            }
        }

        btnGirar.setOnClickListener {
            girarParaPremio()
        }

        btnVoltarMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun girarParaPremio() {
        if (listaPremios.isEmpty()) return

        btnGirar.isEnabled = false
        btnGirar.text = "Girando..."
        tvResultado.text = "Sorteando..."

        // 1. Sorteia usando a lista ATUALIZADA
        val indexVencedor = Random.nextInt(listaPremios.size)
        val premioGanho = listaPremios[indexVencedor]

        // 2. Calcula Rotação
        val anguloPorFatia = 360f / listaPremios.size
        val grausParaParar = 270f - (indexVencedor * anguloPorFatia) - (anguloPorFatia / 2)
        val grausTotais = 3600f + grausParaParar

        // 3. Animação
        val animation = RotateAnimation(
            0f, grausTotais,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation.duration = 4000
        animation.fillAfter = true
        animation.interpolator = DecelerateInterpolator()

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                tvResultado.text = "Você ganhou: $premioGanho"
                Toast.makeText(applicationContext, "Resultado: $premioGanho", Toast.LENGTH_LONG).show()

                // LOGICA DE REPETIÇÃO (Verifica se o nome contém "Extra" ou "Tente")
                // Usamos .contains para ser mais flexível (ex: "Chance Extra" funciona)
                if (premioGanho.contains("Extra", ignoreCase = true) ||
                    premioGanho.contains("Tente de Novo", ignoreCase = true)) {

                    btnGirar.isEnabled = true
                    btnGirar.text = "GIRAR NOVAMENTE!"
                } else {
                    btnGirar.isEnabled = false
                    btnGirar.text = "FIM DE JOGO"
                    btnVoltarMenu.visibility = View.VISIBLE
                }
            }
        })

        roletaView.startAnimation(animation)
    }
}