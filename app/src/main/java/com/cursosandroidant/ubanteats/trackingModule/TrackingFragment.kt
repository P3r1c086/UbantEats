package com.cursosandroidant.ubanteats.trackingModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cursosandroidant.ubanteats.R
import com.cursosandroidant.ubanteats.databinding.FragmentTrackingBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis


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