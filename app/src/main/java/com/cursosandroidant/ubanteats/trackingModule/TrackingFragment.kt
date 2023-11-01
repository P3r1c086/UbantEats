package com.cursosandroidant.ubanteats.trackingModule

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.cursosandroidant.ubanteats.R
import com.cursosandroidant.ubanteats.common.dataAccess.FakeDatabase
import com.cursosandroidant.ubanteats.common.utils.MapUtils
import com.cursosandroidant.ubanteats.databinding.FragmentTrackingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.google.maps.android.SphericalUtil
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

    //En esta variable vamos a guardar todas las posiciones del repartidor para luego hacer la ruta
    // con las polilineas
    private var locations = mutableListOf<LatLng>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.locations.run {
                this.forEach { locations.add(LatLng(it.latitude, it.longitude)) }
                //Aqui comenzamos a dibujar la polilinea
                Log.i("fused location provider", "onLocationsResult: $locations")
            }
        }
    }
    
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

        MapUtils.setupMarkersData(requireActivity(),
            TrackingFragmentArgs.fromBundle(requireArguments()).totalProducts)
    }

    private fun setupButtons() {
        binding.btnFinish.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_trackingFragment_to_productsFragment)
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        //referencia global
        map = googleMap
        //todo remove this
        map.isMyLocationEnabled = true

        MapUtils.setupMap(requireActivity(), map)//como es un fragmento pongo requireActivity como contexto

        //todo mover esto
        calcRealDistance(MapUtils.getOriginDelivery())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun calcRealDistance(location: LatLng){
        //para esto se han agreagado dos librerias propias de GoogleMaps utils
        //El metodo computeDistanceBetween (de la libreria SphericalUtil) me pide dos posiciones Latlan.
        // La primera es el origen(la que va a ir cambiando) y la segunda es el destino
        val distance = SphericalUtil.computeDistanceBetween(location, MapUtils.getDestinationDelivery())
        //seteo la distancia el el textView
        binding.tvDistance.text = getString(R.string.tracking_distance, MapUtils.formatDistance(distance))
        //distancia a la que creamos prudente que se pueda finalizar. De momento pongo 60 metros
        binding.btnFinish.isEnabled = distance < 60
    }
}