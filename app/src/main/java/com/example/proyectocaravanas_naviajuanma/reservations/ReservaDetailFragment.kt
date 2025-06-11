package com.example.proyectocaravanas_naviajuanma.reservations

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.proyectocaravanas_naviajuanma.API.ApiService
import com.example.proyectocaravanas_naviajuanma.API.RetrofitClient
import com.example.proyectocaravanas_naviajuanma.Models.Reserva
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.R
import com.example.proyectocaravanas_naviajuanma.databinding.FragmentReservaDetailBinding

class ReservaDetailFragment : Fragment() {
    private var _binding: FragmentReservaDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReservaViewModel by lazy {
        val apiService = RetrofitClient.instance
        ViewModelProvider(this, ReservaViewModelFactory(apiService)).get(ReservaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reservaId = arguments?.getInt("reservaId") ?: -1
        if (reservaId == -1) {
            showError("ID de reserva no válido")
            return
        }

        val token = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("token", null) ?: run {
            showError("No autenticado")
            findNavController().navigate(R.id.loginFragment)
            return
        }

        viewModel.getReserva("Bearer $token", reservaId)
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.reserva.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> resource.data?.let { showReserva(it) } ?: showError("Datos de reserva inválidos")
                is Resource.Error -> showError(resource.message ?: "Error desconocido")
            }
        }
    }

    private fun showReserva(reserva: Reserva) {
        showLoading(false)
        with(binding) {
            tvModelo.text = "Modelo: ${reserva.vehiculo?.modelo ?: "No disponible"}"
            tvFechas.text = "Fechas: ${reserva.fecha_inicio} a ${reserva.fecha_fin}"
            tvPrecioTotal.text = "Precio total: %.2f €".format(reserva.precio_total)
            tvEstado.text = "Estado: ${reserva.estado}"
        }
    }

    private fun showLoading(isLoading: Boolean) {

        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun setupListeners() {
        binding.btnVolver.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}