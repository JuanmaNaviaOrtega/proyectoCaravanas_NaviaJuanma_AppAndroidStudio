package com.example.proyectocaravanas_naviajuanma
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.proyectocaravanas_naviajuanma.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Verificación de token
        val token = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("token", null)

        if (token == null) {
            findNavController().navigate(R.id.action_global_loginFragment)
            return
        }

        // Configurar clicks
        binding.cardReservas.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_reservasFragment)
        }

        binding.cardVehiculos.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_vehiculosDisponiblesFragment)
        }

        binding.cardProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.cardListadoCaravanas.setOnClickListener {
           findNavController().navigate(R.id.action_homeFragment_to_listadoCaravanasFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}