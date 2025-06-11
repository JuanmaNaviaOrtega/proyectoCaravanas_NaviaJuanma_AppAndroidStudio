package com.example.proyectocaravanas_naviajuanma.vehicles

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
import com.example.proyectocaravanas_naviajuanma.databinding.FragmentVehiculosBinding
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.R
import com.example.proyectocaravanas_naviajuanma.vehicles.adapter.VehiculosAdapter

class VehiculosFragment : Fragment() {
    private var _binding: FragmentVehiculosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VehiculoViewModel by viewModels()
    private lateinit var adapter: VehiculosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVehiculosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchVehiculos()
    }

    private fun setupRecyclerView() {
        adapter = VehiculosAdapter { vehiculo ->
        }

        binding.recyclerViewVehiculos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewVehiculos.adapter = adapter
    }

    private fun fetchVehiculos() {
        val token = getAuthToken()
        if (token != null) {
            viewModel.getAllVehiculos("Bearer $token")
            viewModel.allVehiculos.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { vehiculos ->
                            adapter.submitList(vehiculos)
                            binding.tvEmpty.visibility = if (vehiculos.isEmpty()) View.VISIBLE else View.GONE
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showError(resource.message ?: "Error desconocido")
                    }
                }
            }
        } else {
            showError("No autenticado")
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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