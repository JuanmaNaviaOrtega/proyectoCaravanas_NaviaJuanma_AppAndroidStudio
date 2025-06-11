package com.example.proyectocaravanas_naviajuanma.vehicles

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.databinding.FragmentVehiculosDisponiblesBinding
import com.example.proyectocaravanas_naviajuanma.vehicles.adapter.VehiculosAdapter
import java.util.Calendar

class VehiculosDisponiblesFragment : Fragment() {

    private var _binding: FragmentVehiculosDisponiblesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VehiculoViewModel by viewModels()
    private lateinit var adapter: VehiculosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVehiculosDisponiblesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePickers()
        setupRecyclerView()
        setupObservers()
        binding.btnBuscar.setOnClickListener {
            val fechaInicio = binding.etFechaInicio.text.toString()
            val fechaFin = binding.etFechaFin.text.toString()

            if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, selecciona ambas fechas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val token = getAuthToken()
            if (token != null) {
                viewModel.getVehiculosDisponibles(token, fechaInicio, fechaFin)
            } else {
                Toast.makeText(requireContext(), "No autenticado", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupRecyclerView() {
        adapter = VehiculosAdapter { vehiculo ->
            // Navegar al fragmento de creación de reserva
            val action = VehiculosDisponiblesFragmentDirections.actionVehiculosDisponiblesFragmentToCreateReservaFragment(
                vehiculo.id,
                vehiculo.modelo,
                vehiculo.precio_dia.toFloat(),
                binding.etFechaInicio.text.toString(),
                binding.etFechaFin.text.toString()
            )
            findNavController().navigate(action)
        }

        binding.recyclerViewVehiculos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewVehiculos.adapter = adapter
    }

    private fun setupDatePickers() {
        binding.etFechaInicio.setOnClickListener {
            showDatePickerDialog(true)
        }

        binding.etFechaFin.setOnClickListener {
            showDatePickerDialog(false)
        }
    }
    private fun getAuthToken(): String? {
        val sharedPref = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedDate = "$year-${month + 1}-$day"
                if (isStartDate) {
                    binding.etFechaInicio.setText(selectedDate)
                } else {
                    binding.etFechaFin.setText(selectedDate)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = calendar.timeInMillis
            show()
        }
    }


    private fun setupObservers() {
        viewModel.vehiculos.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}