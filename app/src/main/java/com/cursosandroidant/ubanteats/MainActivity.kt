package com.cursosandroidant.ubanteats

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.cursosandroidant.ubanteats.common.utils.PermissionUtils
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

    private var permissionDenied = false
    
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

        checkLocationPermissions()
    }
    @SuppressLint("MissingPermission")
    private fun checkLocationPermissions() {
        //primero verificamos los permisos para ver si ya los tenemos otorgados o no
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED ){
//            //una vez comprobado que los permisos estan otorgados, activamos isMyLocationEnabled
//            map.isMyLocationEnabled = true//de momento suprimimos el error con una anotacion en la funcion
//            //si ya se han otorgado los permisos y activado isMyLocationEnabled se pone return para
//            // salir del metodo
            return
        }
        //En caso de que no se hayan otorgado los permisos, recordamos al usuario que requerimos esos
        // permisos
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)){
            PermissionUtils.RationaleDialog
                .newInstance(LOCATION_PERMISSION_REQUEST_CODE, true)
                .show(supportFragmentManager, "dialog")
            return
        }
        //Si el propio sistema no solicito ningun permiso con el anterior if, lo hacemos de forma mas forzosa
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, android.Manifest.permission.ACCESS_FINE_LOCATION)
            || PermissionUtils.isPermissionGranted(permissions, grantResults, android.Manifest.permission.ACCESS_COARSE_LOCATION)){
            //Si despues de que el usuario ya vio nuestro dialogo solicitando los permisos y cae en
            // este if, tenemos que verificar con permissions que obtuvimos ambos permisos o solamente
            // alguno, en caso afirmativo, con uno que tengamos podemos empezar a trabajar dentro de la app
            checkLocationPermissions()
        } else {
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied){
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Se le muestra al usuario un dialog explicando al usuario porque se ha perdido ese permiso de ubicacion
     */
    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true)
            .show(supportFragmentManager, "dialog")
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 21//le damos cualquier num entero
    }
}