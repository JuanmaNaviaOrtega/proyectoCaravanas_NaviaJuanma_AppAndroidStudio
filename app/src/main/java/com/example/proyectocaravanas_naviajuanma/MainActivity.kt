package com.example.proyectocaravanas_naviajuanma

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.proyectocaravanas_naviajuanma.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar la barra de herramientas
        setSupportActionBar(binding.toolbar)

        // Configurar el controlador de navegación
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configuración para el AppBar con Navigation
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Configurar el NavigationView
        binding.navView?.let {
            it.setupWithNavController(navController)
        }

        binding.navView?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> navController.navigate(R.id.homeFragment)
                R.id.nav_reservas -> navController.navigate(R.id.reservasFragment)
                R.id.nav_vehiculos -> navController.navigate(R.id.vehiculosDisponiblesFragment)
                R.id.nav_listado_caravanas -> navController.navigate(R.id.listadoCaravanasFragment)
                R.id.nav_perfil -> navController.navigate(R.id.profileFragment)
                R.id.nav_logout -> {
                    // Lógica para cerrar sesión
                    findNavController(
                        viewId = TODO()
                    ).navigate(R.id.loginFragment)
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    
}