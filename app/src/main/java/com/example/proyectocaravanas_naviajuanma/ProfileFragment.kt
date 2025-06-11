package com.example.proyectocaravanas_naviajuanma

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.auth0.android.jwt.JWT
import com.example.proyectocaravanas_naviajuanma.Auth.AuthViewModel
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.Models.User
import com.example.proyectocaravanas_naviajuanma.databinding.FragmentProfileBinding
import com.google.gson.Gson

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = getAuthToken()
        if (token != null) {
            viewModel.getProfile("Bearer $token")
            viewModel.profile.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { user ->
                            binding.tvName.text = user.name
                            binding.tvEmail.text = user.email

                            // Teléfono
                            if (!user.telefono.isNullOrBlank()) {
                                binding.tvPhone.visibility = View.VISIBLE
                                binding.tvPhone.text = user.telefono
                            } else {
                                binding.tvPhone.visibility = View.GONE
                            }

                            // Dirección
                            if (!user.direccion.isNullOrBlank()) {
                                binding.tvAddress.visibility = View.VISIBLE
                                binding.tvAddress.text = user.direccion
                            } else {
                                binding.tvAddress.visibility = View.GONE
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val sharedPref = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("token")
            apply()
        }
        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
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