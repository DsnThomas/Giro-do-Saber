package com.thomasdias.quizroleta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thomasdias.quizroleta.data.Pergunta

class PerguntaAdapter(
    private val onDeletarClick: (Pergunta) -> Unit
) : RecyclerView.Adapter<PerguntaAdapter.PerguntaViewHolder>() {

    private var listaPerguntas = emptyList<Pergunta>()

    class PerguntaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Estamos reutilizando o layout 'item_lista.xml' que criamos para os prêmios
        val tvEnunciado: TextView = itemView.findViewById(R.id.tvNomeItem)
        val btnDeletar: ImageButton = itemView.findViewById(R.id.btnDeletar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerguntaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lista, parent, false)
        return PerguntaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PerguntaViewHolder, position: Int) {
        val perguntaAtual = listaPerguntas[position]

        // Mostra o enunciado e qual é a resposta correta (A, B, C ou D)
        val letraCorreta = when(perguntaAtual.indiceCorreta) {
            0 -> "A"
            1 -> "B"
            2 -> "C"
            else -> "D"
        }

        holder.tvEnunciado.text = "${perguntaAtual.enunciado} (Resp: $letraCorreta)"

        holder.btnDeletar.setOnClickListener {
            onDeletarClick(perguntaAtual)
        }
    }

    override fun getItemCount() = listaPerguntas.size

    fun atualizarLista(novasPerguntas: List<Pergunta>) {
        listaPerguntas = novasPerguntas
        notifyDataSetChanged()
    }
}