package com.example.proyectocaravanas_naviajuanma.reservations.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectocaravanas_naviajuanma.Models.Reserva
import com.example.proyectocaravanas_naviajuanma.databinding.ItemReservaBinding

class ReservasAdapter(
    private val onItemClick: (Reserva) -> Unit
) : ListAdapter<Reserva, ReservasAdapter.ReservaViewHolder>(ReservaDiffCallback()) {

    // Añade esta propiedad para el long click
    var onReservaLongClick: ((Reserva) -> Unit)? = null

    inner class ReservaViewHolder(private val binding: ItemReservaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reserva: Reserva) {
            with(binding) {
                tvModelo.text = reserva.vehiculo?.modelo ?: "Modelo no disponible"
                tvFechas.text = "${reserva.fecha_inicio} - ${reserva.fecha_fin}"
                tvPrecio.text = "%.2f€".format(reserva.precio_total)
                root.setOnClickListener { onItemClick(reserva) }
                // Long click para editar
                root.setOnLongClickListener {
                    onReservaLongClick?.invoke(reserva)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val binding = ItemReservaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReservaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ReservaDiffCallback : DiffUtil.ItemCallback<Reserva>() {
        override fun areItemsTheSame(oldItem: Reserva, newItem: Reserva): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reserva, newItem: Reserva): Boolean {
            return oldItem == newItem
        }
    }
}