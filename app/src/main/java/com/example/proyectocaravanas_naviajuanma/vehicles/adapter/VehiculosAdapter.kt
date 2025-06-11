package com.example.proyectocaravanas_naviajuanma.vehicles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectocaravanas_naviajuanma.Models.Vehiculo
import com.example.proyectocaravanas_naviajuanma.databinding.ItemVehiculoBinding

class VehiculosAdapter(
    private val onItemClick: (Vehiculo) -> Unit
) : ListAdapter<Vehiculo, VehiculosAdapter.VehiculoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiculoViewHolder {
        val binding = ItemVehiculoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehiculoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehiculoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VehiculoViewHolder(private val binding: ItemVehiculoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vehiculo: Vehiculo) {
            binding.tvModelo.text = vehiculo.modelo
            binding.tvPrecioDia.text = "${vehiculo.precio_dia} €/día"
            binding.tvDisponible.text = if (vehiculo.disponible) "Disponible" else "No disponible"

            // Detectar clic en el vehículo
            binding.root.setOnClickListener {
                onItemClick(vehiculo)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Vehiculo>() {
        override fun areItemsTheSame(oldItem: Vehiculo, newItem: Vehiculo): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Vehiculo, newItem: Vehiculo): Boolean = oldItem == newItem
    }
}