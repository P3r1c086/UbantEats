package com.cursosandroidant.ubanteats.trackingModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.cursosandroidant.ubanteats.R
import com.cursosandroidant.ubanteats.common.dataAccess.FakeDatabase
import com.cursosandroidant.ubanteats.databinding.FragmentTrackingBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/****
 * Project: Ubant Eats
 * From: com.cursosandroidant.ubanteats.trackingModule
 * Created by Alain Nicolás Tello on 12/10/22 at 19:16
 * All rights reserved 2022.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Web: www.alainnicolastello.com
 ***/
class TrackingFragment : Fragment(), OnMapReadyCallback{
    
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialFadeThrough()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTrackingBinding.inflate(LayoutInflater.from(context))

        //vincular nuestro fragmento de la vista con el mapa cargado y lo casteamos a SupportMapFragment
        val mapFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        //le sincronizamos el mapa. Usamos getMapAsync para que en segundo plano se vincule y se
        // notifique posteriormente con onMapReady()
        mapFragment?.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        setupDeliveryUserToUi()
    }

    private fun setupButtons() {
        //simulo con una corrutina un delay para habilitar despues un boton
        lifecycleScope.launch {
            delay(1_000)
            binding.btnFinish.isEnabled = true
        }
    }

    private fun setupDeliveryUserToUi() {
        //Consulto el usuario
        val user = FakeDatabase.getDeliveryUser()
        with(binding){
            tvName.setText(getString(R.string.traking_name, user.name))
            Glide.with(this@TrackingFragment)
                .load(user.photoUrl)
                .circleCrop()
                .into(imgPhoto)
        }
    }

    /**
     * Metodo para que, una vez inicializado el mapa y bien configurado, podamos hacer uso de el
     */
    override fun onMapReady(googleMap: GoogleMap) {
        //referencia global
        map = googleMap
        //algunas configuraciones
        map.uiSettings.isZoomControlsEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}