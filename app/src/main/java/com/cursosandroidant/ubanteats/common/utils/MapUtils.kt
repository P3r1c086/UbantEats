package com.cursosandroidant.ubanteats.common.utils

import android.content.Context
import android.graphics.Bitmap
import com.cursosandroidant.ubanteats.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

object MapUtils {

    private var iconDeliveryMarker: Bitmap? = null
    private var iconDestinationMarker: Bitmap? = null

    //Se requiere el la variable para el marcador del repartidor porque se va a estar moviendo,
    // mientras que el destino es fijo
    private var deliveryMarker: Marker? = null

    //Calle Sol
    fun getDestinationDelivery(): LatLng = LatLng(37.176313982382894, -3.6093397191763383)

    //La Tertulia
    fun getOriginDelivery(): LatLng = LatLng(37.17756113402987, -3.6072123689234457)
    fun setupMap(context: Context, map: GoogleMap){
        //agrego una posicion que representa el destino hacia donde se tiene que entregar el paquete
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(getDestinationDelivery(), 17f))
        map.uiSettings.apply {
            isMyLocationButtonEnabled = false
            isZoomGesturesEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false //esto es para la inclinacion
            isMapToolbarEnabled = false
        }
        setupMapStyle(context, map)

        addDeliveryMarker(map, getOriginDelivery())
        addDestinationMarker(map, getDestinationDelivery())
    }

    private fun setupMapStyle(context: Context, map: GoogleMap) {
        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //todo: remove this
        //con esto configuramos los iconos
        setupMarkersData(context)


    }

    private fun setupMarkersData(context: Context){
        //configuramos los iconos de repartidor y destino
        Utils.getBitmapFromVector(context, R.drawable.ic_delivery_motorbike_32)?.let {
            iconDeliveryMarker = it
        }
        Utils.getBitmapFromVector(context, R.drawable.ic_destination_flag_32)?.let {
            iconDestinationMarker = it
        }
    }

    private fun addDeliveryMarker(map: GoogleMap, location: LatLng){
        iconDeliveryMarker?.let {
            deliveryMarker = map.addMarker(MarkerOptions()
                //Nuestro repartidor se va a ir moviendo en el tiempo y la distancia, por lo que la
                // location tiene que ser variable mutable
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(it))
                .anchor(0.5f, 0.5f)
                .title("12 rosquillas")
            )
        }
    }
    private fun addDestinationMarker(map: GoogleMap, location: LatLng){
        //Una vez configurados los iconos(pasandolos de imagen de vectores a bitmap), los agregamos
        // al marcador
        iconDestinationMarker?.let {
            map.addMarker(MarkerOptions()
                .position(location)
                //con esto centro el icono. Las medidas varian en funcion del icono, en este caso
                // quiero que lo que señale sea la base de la bandera
                .anchor(0.3f, 1f)
                .icon(BitmapDescriptorFactory.fromBitmap(it)))
        }
    }

    //rawDistance es distancia en crudo
    fun formatDistance(rawDistance: Double): String {
        var distance = rawDistance.toInt()
        val unit = if (distance > 1_000) {//estamos trabajando en kilometros
            //antes distance se tiene que reasignar, ya que va a ser el resultado de..
            distance /= 1_000
            "km"
        }else{//si no es mas de 1000, significa que estamos trabajando con metros, la cual no necesita
            // ser procesada porque ya viene en metros
            "m"
        }
        //aqui recibo un argumento que seria de tipo decimal y despues un segundo argumento en formato string
        return String.format("%d%s", distance, unit)
    }
}