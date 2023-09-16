package com.eflm.practica2.ui.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eflm.practica2.data.remote.model.AlimentoDto
import com.eflm.practica2.databinding.AlimentElementBinding

class AlimentsAdapter(
    private val aliments: List<AlimentoDto>,
    private val onAlimentClick: (AlimentoDto) -> Unit
): RecyclerView.Adapter<AlimentsAdapter.ViewHolder>() {
    class ViewHolder(private val binding: AlimentElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivThumbnail = binding.ivThumbnail

        fun bind(aliment: AlimentoDto){
            binding.tvTitle.text = aliment.nombre
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AlimentElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = aliments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ali = aliments[position]

        holder.bind(ali)

        Glide.with(holder.itemView.context)
            .load(ali.imagen)
            .into(holder.ivThumbnail)

        //Procesamiento del clic al elemento
        holder.itemView.setOnClickListener {
            onAlimentClick(ali)
        }
    }
}