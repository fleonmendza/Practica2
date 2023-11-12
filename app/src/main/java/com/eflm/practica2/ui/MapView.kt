package com.eflm.practica2.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eflm.practica2.R
import com.eflm.practica2.databinding.ActivityMapViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapView : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapViewBinding

    private var lat: Double? = null
    private var long: Double? = null
    private var name: String? = null


    //Para Google Maps
    private lateinit var map: GoogleMap

    //Para los permisos
    private var fineLocationPermissionGranted = false

    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            //Se concedió el permiso
            actionPermissionGranted()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle("Permiso requerido")
                    .setMessage("Se necesita el permiso para poder ubicar la posición del usuario en el mapa")
                    .setPositiveButton("Entendido") { _, _ ->
                        updateOrRequestPermissions()
                    }
                    .setNegativeButton("Salir") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .create()
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "El permiso de ubicación se ha negado permanentemente",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    private fun actionPermissionGranted() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //Manejar el permiso
            //Pero en este caso ya no es necesario
            return
        }
        map.isMyLocationEnabled = true


//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        locationManager.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER,
//            2000,
//            10f,
//            this
//        )
    }

    private fun updateOrRequestPermissions() {
        //Revisando el permiso
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        fineLocationPermissionGranted = hasFineLocationPermission

        if (!fineLocationPermissionGranted) {
            //Pedimos el permiso
            permissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            //Tenemos los permisos
            actionPermissionGranted()
        }

    }

    override fun onMapReady(googleMap: GoogleMap ) {
        map = googleMap

        lat = intent.getDoubleExtra("EXTRA_LAT", 0.0)
        long = intent.getDoubleExtra("EXTRA_LONG", 0.0)
        name = intent.getStringExtra("EXTRA_NAME")

        Toast.makeText(
            this,
            "$lat, $long",
            Toast.LENGTH_LONG
        ).show()
        createMarker(lat!!, long!!, name!!)


        updateOrRequestPermissions()
        map.setOnMapLongClickListener { position ->
            val marker = MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.comida))
            map.addMarker(marker)
        }
    }

    private fun createMarker(lat:Double,long:Double, name:String){

        val coordinates = LatLng(lat, long)
        val marker = MarkerOptions()
            .position(coordinates)
            .title(name)
            //.snippet("Cursos y diplomados en TIC")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.comida))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )

    }

    override fun onRestart() {
        super.onRestart()
        if(!::map.isInitialized) return
        if(!fineLocationPermissionGranted)
            updateOrRequestPermissions()
    }











}