package com.cursosandroidant.ubanteats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.cursosandroidant.ubanteats.databinding.ActivityMainBinding

/****
 * Project: Ubant Eats
 * From: com.cursosandroidant.ubanteats
 * Created by Alain Nicolás Tello on 14/10/22 at 15:15
 * All rights reserved 2022.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Web: www.alainnicolastello.com
 ***/
class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    
    private lateinit var binding: ActivityMainBinding
    
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        
        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            binding.toolbar.title = destination.label
            binding.toolbar.navigationIcon = null
        }
    }
}