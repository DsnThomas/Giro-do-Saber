package com.thomasdias.quizroleta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thomasdias.quizroleta.data.Premio

class PremioAdapter(
    private val onDeletarClick: (Premio) -> Unit
) : RecyclerView.Adapter<PremioAdapter.PremioViewHolder>() {

    private var listaPremios = emptyList<Premio>()

    class PremioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNomeItem)
        val btnDeletar: ImageButton = itemView.findViewById(R.id.btnDeletar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lista, parent, false)
        return PremioViewHolder(view)
    }

    override fun onBindViewHolder(holder: PremioViewHolder, position: Int) {
        val premioAtual = listaPremios[position]
        holder.tvNome.text = premioAtual.nome

        holder.btnDeletar.setOnClickListener {
            onDeletarClick(premioAtual)
        }
    }

    override fun getItemCount() = listaPremios.size

    fun atualizarLista(novosPremios: List<Premio>) {
        listaPremios = novosPremios
        notifyDataSetChanged()
    }
}