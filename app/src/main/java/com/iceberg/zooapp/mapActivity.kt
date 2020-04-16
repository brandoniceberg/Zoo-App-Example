package com.iceberg.zooapp

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Geometry
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_map.*


class mapActivity: AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mapView: MapView
    private val REQUEST_CODE = 5694

    var animalName: String? = null
    var animalImage: String? = null
    var animalHabitat: String? = null
    var animalDescription: Int? = null
    var animalLongitude: Double? = null
    var animalLatitude: Double? = null
    var animalCoordiates: LatLng? = null
    var animalStatus: Int? = null
    var animalBioName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_map)

        val cardView = informationCard
        val map = map
        cardView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        map.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        //import extras from previous activity
        val bundle: Bundle = intent.extras!!
        animalName = bundle.getString("name")
        animalDescription = bundle.getInt("description")
        animalImage = bundle.getString("image")
        animalHabitat = bundle.getString("habitat")
        animalStatus = bundle.getInt("status")
        animalLatitude = bundle.getDouble("latitude")
        animalLongitude = bundle.getDouble("longitude")
        animalBioName = bundle.getString("bioname")
        animalCoordiates = LatLng(animalLatitude!!, animalLongitude!!)
        val animalFood = bundle.getInt("food")

        //Fill views with information
        Glide.with(this).load(animalImage).into(animalImageView)
        animalNameTextView.text = animalName
        descriptionTextView.text = getString(animalDescription!!)
        habitatTextView.text = "Native Habitat: $animalHabitat"
        bionameTextView.text = animalBioName
        foodImageView.setImageResource(animalFood)
        statusImageView.setImageResource(animalStatus!!)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        supportActionBar!!.title = animalName
        supportActionBar!!.setHomeButtonEnabled(true)

        mapView = map
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        checkPermissions()
    }


    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //Permission wasn't granted, request permission
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            }
        }
        return
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("LOCATION PERMISSION", "Permission granted.")
                } else {
                    Toast.makeText(this, "Location permission was denied. Some features may not be available.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        val symbolLayerIconFeatureList: ArrayList<Feature> = arrayListOf()
        val entrance = Feature.fromGeometry(Point.fromLngLat(36.2130649, -95.9056249))
        //Show entrance on
        symbolLayerIconFeatureList.add(entrance)

        //Define map settings
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.uiSettings.isTiltGesturesEnabled = false
        mapboxMap.uiSettings.isRotateGesturesEnabled = false
        mapboxMap.uiSettings.isLogoEnabled = false
        mapboxMap.uiSettings.attributionGravity = Gravity.BOTTOM
        mapboxMap.setMaxZoomPreference(18.50)
        mapboxMap.setMinZoomPreference(15.60)
        mapboxMap.uiSettings.setAttributionMargins(0, 0 , 0 , 0)

        mapboxMap.setStyle(Style.Builder().fromUri("mapbox://styles/brandoniceberg/ck72sen6g1mju1itez0o48916"))


        val animalLocation = com.mapbox.mapboxsdk.geometry.LatLng(animalLatitude!!, animalLongitude!!)
        mapboxMap.addMarker(MarkerOptions().position(animalLocation).title("$animalName Exhibit"))
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(animalLocation, 18.0))

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
