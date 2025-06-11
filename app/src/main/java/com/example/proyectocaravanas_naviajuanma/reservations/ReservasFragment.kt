package com.example.proyectocaravanas_naviajuanma.reservations

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectocaravanas_naviajuanma.API.RetrofitClient
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.R
import com.example.proyectocaravanas_naviajuanma.databinding.FragmentReservasBinding
import com.example.proyectocaravanas_naviajuanma.reservations.adapters.ReservasAdapter

class ReservasFragment : Fragment() {
    private var _binding: FragmentReservasBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReservasAdapter

    private val viewModel: ReservaViewModel by lazy {
        val apiService = RetrofitClient.instance
        ViewModelProvider(this, ReservaViewModelFactory(apiService)).get(ReservaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        fetchReservas()
        setupSwipeToDelete()
        setupLongClickEdit()
    }

    private fun fetchReservas() {
        val token = getAuthToken()
        if (token != null) {
            viewModel.getReservas("Bearer $token")
            viewModel.reservas.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)
                        resource.data?.let { reservas ->
                            adapter.submitList(reservas)
                            binding.tvEmpty.visibility = if (reservas.isEmpty()) View.VISIBLE else View.GONE
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showError(resource.message ?: "Error desconocido")
                    }
                }
            }
        } else {
            showError("No autenticado")
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    private fun setupRecyclerView() {
        adapter = ReservasAdapter { reserva ->
            val bundle = Bundle().apply {
                putInt("reservaId", reserva.id)
            }
            findNavController().navigate(R.id.action_reservasFragment_to_reservaDetailFragment, bundle)
        }

        binding.recyclerViewReservas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ReservasFragment.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupListeners() {
        binding.fabAddReserva.setOnClickListener {
            findNavController().navigate(R.id.action_reservasFragment_to_vehiculosDisponiblesFragment)
        }
    }

    // Swipe para borrar
    private fun setupSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val reserva = adapter.currentList[position]
                AlertDialog.Builder(requireContext())
                    .setTitle("Borrar reserva")
                    .setMessage("¿Seguro que quieres borrar esta reserva?")
                    .setPositiveButton("Sí") { _, _ ->
                        val token = getAuthToken()
                        if (token != null) {
                            viewModel.borrarReserva("Bearer $token", reserva.id, {

                                fetchReservas()
                            }, { errorMsg ->

                                showError(errorMsg)
                            })
                        }
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        adapter.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        adapter.notifyItemChanged(position)
                    }
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewReservas)
    }

    // Long press para editar
    private fun setupLongClickEdit() {
        adapter.onReservaLongClick = { reserva ->
            val bundle = Bundle().apply {
                putInt("reservaId", reserva.id)
            }
            findNavController().navigate(R.id.action_reservasFragment_to_editarReservaFragment, bundle)
        }
    }
}