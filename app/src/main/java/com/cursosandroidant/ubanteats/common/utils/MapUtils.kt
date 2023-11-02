package com.cursosandroidant.ubanteats.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.cursosandroidant.ubanteats.R
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap

object MapUtils {

    private var iconDeliveryMarker: Bitmap? = null
    private var iconDestinationMarker: Bitmap? = null

    //Se requiere el la variable para el marcador del repartidor porque se va a estar moviendo,
    // mientras que el destino es fijo
    private var deliveryMarker: Marker? = null
    private var totalProducts: Int = 0

    //configuramos locationRequest
    val locationRequest = LocationRequest.create()
        //setInterval puede ahorrar bateria si son grandes destancias, por ejemplo un viaje entre
        // ciudades en la cual por ejemplo cada minuto o treinta segundos nuestro proveedor fusionado
        // nos puede dar una mejor ubicacion de acuerdo a la mas reciente con la que cuente ya sea
        // por GPS o WIFI. Este va a funcionar solo si hay permisos de ubicacion aproximada.
        .setInterval(5_000)
        //setFastestInterval indica cual es el tiempo minimo que se necesita para que el usuario pueda
        // procesar la ubicacion, es decir, no tiene sentido que intentemos actualizar cada 100
        // milisegundos por ejemplo, si la app tarda un segundo y medio en consultar el tiempo estimado,
        // dibujar la polilinea, los marcadores, quizas enviar alguna notification,etc.. Por esta razon,
        // tenemos que calcular un numero que sea razonable.
        .setFastestInterval(2_000)
        //En setPriority le estamos configurando que solicitamos intervalos cortos para obtener una
        // ubicacion
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)

    //Casa
    fun getDestinationDelivery(): LatLng = LatLng(37.123678269706275, -3.6710739733400524)

    //Colegio bilingüe
    fun getOriginDelivery(): LatLng = LatLng(37.12427339013188, -3.66831807095581)
    fun setupMap(context: Context, map: GoogleMap){
        //agrego una posicion que representa el destino hacia donde se tiene que entregar el paquete
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(getDestinationDelivery(), 17f))
        map.uiSettings.apply {
            isMyLocationButtonEnabled = false
            isZoomGesturesEnabled = true
            isRotateGesturesEnabled = true
            isTiltGesturesEnabled = false //esto es para la inclinacion
            isMapToolbarEnabled = false
        }
        setupMapStyle(context, map)

        addDeliveryMarker(context, map, getOriginDelivery())
        addDestinationMarker(map, getDestinationDelivery())
    }

    private fun setupMapStyle(context: Context, map: GoogleMap) {
        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setupMarkersData(context: Context, total: Int){
        //configuramos los iconos de repartidor y destino
        Utils.getBitmapFromVector(context, R.drawable.ic_delivery_motorbike_32)?.let {
            iconDeliveryMarker = it
        }
        Utils.getBitmapFromVector(context, R.drawable.ic_destination_flag_32)?.let {
            iconDestinationMarker = it
        }
        totalProducts = total
    }

    private fun addDeliveryMarker(context: Context, map: GoogleMap, location: LatLng){
        iconDeliveryMarker?.let {
            deliveryMarker = map.addMarker(MarkerOptions()
                //Nuestro repartidor se va a ir moviendo en el tiempo y la distancia, por lo que la
                // location tiene que ser variable mutable
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(it))
                .anchor(0.5f, 0.5f)
                .title(formatTitle(context))
            )
            deliveryMarker?.showInfoWindow()
        }
    }

    private fun formatTitle(context: Context): String {
        var total = ""
        var label = ""
        if (totalProducts == 1){
            total = context.getString(R.string.tracking_total_product)
            label = context.getString(R.string.tracking_label_product)
        }else{
            total = totalProducts.toString()
            label = context.getString(R.string.tracking_label_products)
        }
        return String.format("%s %s", total, label)
    }

    fun addDestinationMarker(map: GoogleMap, location: LatLng){
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

    fun addPolyline(map: GoogleMap, locations: MutableList<LatLng>) {
        map.addPolyline(PolylineOptions()
            .width(16f)
            .color(Color.GREEN)
            .jointType(JointType.ROUND)
            .startCap(RoundCap())
            .addAll(locations))
    }

    fun runDeliveryMap(context: Context, map: GoogleMap, location: LatLng){
        removeOldDeliveryMarker()
        //agregamamos el marcador del repartidor y darle un seguimiento con la camara
        addDeliveryMarker(context, map, location)
        //construimos un Latlng Builder a partir de dos posiciones(origen y destino)
        val builder = LatLngBounds.Builder()
            .include(getDestinationDelivery())
            .include(location)//seria la posicion actual del repartidor
        //para que los marcadores no esten el los bordes de la pantalla o incluso no se vean
        val padding = 256
        //Con esto tendremos nuestra area basada en destino y origen
        val distanceBounds: LatLngBounds = builder.build()
        //Crar una camara que se vaya moviendo de acuerdo a estos bounds y que verifique si nuestro
        // mapa esta cargado
        map.setOnMapLoadedCallback {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(distanceBounds, padding))
        }

    }

    //removemos el marcador del repartidor para que no se vaya pintando de nuevo en cada posicion de la ruta
    private fun removeOldDeliveryMarker() = deliveryMarker?.remove()
}