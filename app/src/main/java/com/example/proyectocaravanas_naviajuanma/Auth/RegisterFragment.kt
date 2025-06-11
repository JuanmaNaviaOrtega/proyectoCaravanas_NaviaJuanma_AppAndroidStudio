package com.example.proyectocaravanas_naviajuanma.Auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.proyectocaravanas_naviajuanma.Models.Resource
import com.example.proyectocaravanas_naviajuanma.R
import com.example.proyectocaravanas_naviajuanma.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(name, email, password)
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    result.data?.let { authResponse ->
                        saveAuthToken(authResponse.token)
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    } ?: run {
                        Toast.makeText(requireContext(), "Error: Datos de registro inválidos", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    val errorMsg = result.message ?: "Error desconocido"
                    Toast.makeText(requireContext(), "Error: $errorMsg", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveAuthToken(token: String) {
        val sharedPref = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("token", token)
            apply()
        }
    }
}