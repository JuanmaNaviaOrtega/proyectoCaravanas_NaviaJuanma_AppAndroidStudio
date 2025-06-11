package com.example.proyectocaravanas_naviajuanma.reservations

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.R
import com.example.proyectocaravanas_naviajuanma.databinding.FragmentCreateReservaBinding
import com.example.proyectocaravanas_naviajuanma.API.RetrofitClient
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateReservaFragment : Fragment() {
    private var _binding: FragmentCreateReservaBinding? = null
    private val binding get() = _binding!!
    private val args: CreateReservaFragmentArgs by navArgs()

    private val viewModel: ReservaViewModel by lazy {
        val apiService = RetrofitClient.instance
        ViewModelProvider(this, ReservaViewModelFactory(apiService)).get(ReservaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReservaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayReservationDetails()
        setupObservers()
    }

    private fun displayReservationDetails() {
        val precioDia = args.precioDia.toDouble()
        val dias = calcularDiasReserva(args.fechaInicio, args.fechaFin)
        val precioTotal = dias * precioDia
        val deposito = precioTotal * 0.2


        binding.tvDetallesVehiculo.text = "Vehículo ID: ${args.vehiculoId}"
        binding.tvFechasReserva.text = "Fechas: ${args.fechaInicio} a ${args.fechaFin}"
        binding.tvPrecioTotal.text = "Precio total: ${formatCurrency(precioTotal)}"
        binding.tvDeposito.text = "Depósito requerido: ${formatCurrency(deposito)}"

        binding.btnConfirmarReserva.setOnClickListener {
            val fechaInicio = args.fechaInicio
            val fechaFin = args.fechaFin
            val error = validarFechas(fechaInicio, fechaFin)
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val token = getAuthToken()
            if (token != null) {
                viewModel.createReserva("Bearer $token", args.vehiculoId, fechaInicio, fechaFin)
            } else {
                Toast.makeText(requireContext(), "No autenticado", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_global_loginFragment)
            }
        }
    }

    private fun setupObservers() {
        viewModel.createReservaResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnConfirmarReserva.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnConfirmarReserva.isEnabled = true
                    Toast.makeText(requireContext(), "Reserva creada correctamente", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack(R.id.reservasFragment, false)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnConfirmarReserva.isEnabled = true
                    val errorMsg = resource.message ?: "Error desconocido"
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun calcularDiasReserva(fechaInicio: String, fechaFin: String): Int {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = format.parse(fechaInicio) ?: return 0
        val endDate = format.parse(fechaFin) ?: return 0
        return ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt()
    }

    private fun validarFechas(fechaInicio: String, fechaFin: String): String? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val hoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val startDate = try { format.parse(fechaInicio) } catch (e: Exception) { null }
        val endDate = try { format.parse(fechaFin) } catch (e: Exception) { null }

        if (startDate == null || endDate == null) return "Fechas no válidas"

        // La fecha de inicio debe ser hoy o posterior
        if (startDate.before(hoy)) return "La fecha de inicio debe ser hoy o posterior"

        // La fecha de fin debe ser posterior a la de inicio
        if (!endDate.after(startDate)) return "La fecha de fin debe ser posterior a la de inicio"

        // Antelación máxima 60 días
        val diffDiasAntelacion = ((startDate.time - hoy.time) / (1000 * 60 * 60 * 24)).toInt()
        if (diffDiasAntelacion > 60) return "Solo se puede reservar con un máximo de 60 días de antelación"

        // Mínimo de días según mes
        val calInicio = Calendar.getInstance().apply { time = startDate }
        val calFin = Calendar.getInstance().apply { time = endDate }
        val mesInicio = calInicio.get(Calendar.MONTH) + 1 // Enero = 0
        val mesFin = calFin.get(Calendar.MONTH) + 1
        val minDias = if (mesInicio == 7 || mesInicio == 8 || mesFin == 7 || mesFin == 8) 7 else 2
        val diffDias = ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt()
        if (diffDias < minDias) return "La reserva debe ser de al menos $minDias días"

        return null // Todo OK
    }

    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("es", "ES")).format(amount)
    }

    private fun getAuthToken(): String? {
        val sharedPref = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}