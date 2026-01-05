package com.thomasdias.quizroleta

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class RoletaView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Lista de prêmios (Pode mudar depois!)
    private var itens = listOf("10 Pontos", "Tente de Novo", "Premio Extra", "Nada", "Jackpot", "Sorte")

    // Cores das fatias (Alternando)
    private val cores = listOf(
        Color.parseColor("#FF5722"), // Laranja
        Color.parseColor("#FFC107"), // Amarelo
        Color.parseColor("#4CAF50"), // Verde
        Color.parseColor("#2196F3"), // Azul
        Color.parseColor("#9C27B0"), // Roxo
        Color.parseColor("#F44336")  // Vermelho
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    private val rectF = RectF() // A área do círculo

    // Função para atualizar os itens de fora
    fun definirItens(novosItens: List<String>) {
        itens = novosItens
        invalidate() // Redesenha a roleta
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centroX = width / 2f
        val centroY = height / 2f
        val raio = min(centroX, centroY) - 20f // Deixa uma margem

        rectF.set(centroX - raio, centroY - raio, centroX + raio, centroY + raio)

        val anguloFatia = 360f / itens.size

        for (i in itens.indices) {
            // 1. Desenhar a Fatia
            paint.color = cores[i % cores.size]
            canvas.drawArc(rectF, i * anguloFatia, anguloFatia, true, paint)

            // 2. Desenhar o Texto
            drawTextCentered(canvas, itens[i], centroX, centroY, raio, i * anguloFatia, anguloFatia)
        }
    }

    private fun drawTextCentered(canvas: Canvas, text: String, cx: Float, cy: Float, raio: Float, startAngle: Float, sweepAngle: Float) {
        // Matemática para colocar o texto no meio da fatia
        val angle = startAngle + sweepAngle / 2
        val rad = Math.toRadians(angle.toDouble())

        // Posição do texto (0.75 significa 75% do caminho para a borda)
        val x = cx + (raio * 0.75f) * cos(rad).toFloat()
        val y = cy + (raio * 0.75f) * sin(rad).toFloat()

        canvas.save()
        canvas.rotate(angle + 90, x, y) // Gira o texto para acompanhar a roda
        canvas.drawText(text, x, y, textPaint)
        canvas.restore()
    }
}