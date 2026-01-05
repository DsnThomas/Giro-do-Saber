package com.thomasdias.quizroleta

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // 1. Iniciar a Música (O Gerente decide se cria ou se já existe)
        MusicManager.iniciarMusica(this)

        // 2. Configurar o Botão de Som
        val btnSom = findViewById<ImageButton>(R.id.btnSom)
        atualizarIconeSom(btnSom) // Garante que o ícone tá certo ao abrir

        btnSom.setOnClickListener {
            val estaMudo = MusicManager.alternarSom()
            // Atualiza o ícone visualmente
            if (estaMudo) {
                btnSom.setImageResource(R.drawable.ic_som_off)
            } else {
                btnSom.setImageResource(R.drawable.ic_som_on)
            }
        }

        val btnIniciar = findViewById<Button>(R.id.btnIniciar)
        val btnPerguntas = findViewById<Button>(R.id.btnPerguntas)

        // Botão Iniciar: Vai para o Jogo (MainActivity)
        btnIniciar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val btnPremios = findViewById<Button>(R.id.btnPremios)
        btnPremios.setOnClickListener {
            val intent = Intent(this, PremiosActivity::class.java)
            startActivity(intent)
        }

        // Botão Perguntas: Vai para a tela de Perguntas (Vamos criar no próximo passo)
        btnPerguntas.setOnClickListener {
            val intent = Intent(this, PerguntasActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // Avisa o MusicManager para parar
        MusicManager.pararMusica()
    }
    private fun atualizarIconeSom(btn: ImageButton) {
        if (MusicManager.isTocando()) {
            btn.setImageResource(R.drawable.ic_som_on)
        } else {
            btn.setImageResource(R.drawable.ic_som_off)
        }
    }

}

