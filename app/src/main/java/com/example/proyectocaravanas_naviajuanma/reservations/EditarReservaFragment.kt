package com.example.proyectocaravanas_naviajuanma.reservations

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.proyectocaravanas_naviajuanma.API.RetrofitClient
import com.example.proyectocaravanas_naviajuanma.Models.Reserva
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.Models.Vehiculo
import com.example.proyectocaravanas_naviajuanma.R
import com.example.proyectocaravanas_naviajuanma.databinding.FragmentEditarReservaBinding
import java.text.SimpleDateFormat
import java.util.*

class EditarReservaFragment : Fragment() {
    private var _binding: FragmentEditarReservaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReservaViewModel
    private lateinit var vehiculos: List<Vehiculo>
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarReservaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val apiService = RetrofitClient.instance
        viewModel = ViewModelProvider(
            this,
            ReservaViewModelFactory(apiService)
        ).get(ReservaViewModel::class.java)

        // Obtener el id de la reserva
        val reservaId = arguments?.getInt("reservaId") ?: run {
            showError("ID de reserva no válido")
            findNavController().popBackStack()
            return
        }

        // Obtener token
        val token = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("token", null) ?: run {
            showError("No autenticado")
            findNavController().navigate(R.id.loginFragment)
            return
        }

        // Cargar vehículos
        viewModel.getAllVehiculos("Bearer $token")
        viewModel.allVehiculos.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        vehiculos = it
                        setupVehiculosSpinner()
                        loadReservaData("Bearer $token", reservaId)
                    } ?: showError("Datos de vehículos no disponibles")
                }
                is Resource.Error -> {
                    showError(resource.message ?: "Error al cargar vehículos")
                }
                is Resource.Loading -> {

                }
            }
        }


        binding.etFechaInicio.setOnClickListener { showDatePicker(true) }
        binding.etFechaFin.setOnClickListener { showDatePicker(false) }

        binding.btnGuardar.setOnClickListener {
            guardarCambios("Bearer $token", reservaId)
        }
    }

    private fun setupVehiculosSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            vehiculos.map { it.modelo }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerVehiculo.adapter = adapter
    }

    private fun loadReservaData(token: String, reservaId: Int) {
        viewModel.getReserva(token, reservaId)
        viewModel.reserva.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { reserva ->
                        // Seleccionar vehículo en spinner
                        val vehiculoIndex = vehiculos.indexOfFirst { it.id == reserva.vehiculo_id }
                        if (vehiculoIndex != -1) {
                            binding.spinnerVehiculo.setSelection(vehiculoIndex)
                        }

                        // Establecer fechas
                        binding.etFechaInicio.setText(reserva.fecha_inicio)
                        binding.etFechaFin.setText(reserva.fecha_fin)
                    }
                }
                is Resource.Error -> {
                    showError(resource.message ?: "Error al cargar reserva")
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    private fun showDatePicker(isStartDate: Boolean) {
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedDate = formatDate(year, month, day)
                if (isStartDate) {
                    binding.etFechaInicio.setText(selectedDate)
                } else {
                    binding.etFechaFin.setText(selectedDate)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun guardarCambios(token: String, reservaId: Int) {
        val vehiculoIndex = binding.spinnerVehiculo.selectedItemPosition
        if (vehiculoIndex == -1) {
            showError("Selecciona un vehículo")
            return
        }

        val fechaInicio = binding.etFechaInicio.text.toString()
        val fechaFin = binding.etFechaFin.text.toString()

        if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            showError("Selecciona ambas fechas")
            return
        }

        val vehiculoId = vehiculos[vehiculoIndex].id

        viewModel.editarReserva(
            token,
            reservaId,
            vehiculoId,
            fechaInicio,
            fechaFin,
            onSuccess = { reserva ->
                Toast.makeText(requireContext(), "Reserva actualizada", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            },
            onError = { errorMsg ->
                showError(errorMsg)
            }
        )
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}