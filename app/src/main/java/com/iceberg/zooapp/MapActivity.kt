package com.iceberg.zooapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.activity_animal_map.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_map.animalImageView
import kotlinx.android.synthetic.main.activity_map.animalNameTextView
import kotlinx.android.synthetic.main.activity_map.bionameTextView
import kotlinx.android.synthetic.main.activity_map.descriptionTextView
import kotlinx.android.synthetic.main.activity_map.foodImageView
import kotlinx.android.synthetic.main.activity_map.habitatTextView
import kotlinx.android.synthetic.main.activity_map.informationCard
import kotlinx.android.synthetic.main.activity_map.statusImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

private const val TAG = "MapActivity"

class MapActivity: AppCompatActivity(), OnMapReadyCallback, PermissionsListener, LocationEngineCallback<LocationEngineResult>{

    private lateinit var permissionsManager: PermissionsManager

    //MapBox Assets
    private lateinit var mapView: MapView

    private lateinit var mapboxMap: MapboxMap
    private var locationEngine: LocationEngine? = null
    private lateinit var originLocation: Point
    private lateinit var desitination : Point


    //Animal Assets
    private lateinit var animalName: String
    private lateinit var bioname: String
    private lateinit var description: String
    private lateinit var animalImgs: ArrayList<String>
    private lateinit var animalHabitat: ArrayList<String>
    private var animalLongitude: Double? = null
    private var animalLatitude: Double? = null
    private var animalCoordiates: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_animal_map)
        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationEngine = LocationEngineProvider.getBestLocationEngine(this)

        informationCard.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mapView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        //import extras from previous activity
        val bundle: Bundle = intent.extras!!
        bundle.apply {
            animalName = this.getString("name")!!
            bioname = this.getString("bioname")!!
            description = this.getString("description")!!
            animalImgs = this.getStringArrayList("imgs")!!
            animalHabitat = this.getStringArrayList("habitat")!!
            animalLongitude = this.getDouble("longitude")
            animalLatitude = this.getDouble("latitude")
        }
        if (animalLatitude != null && animalLongitude != null){
            animalCoordiates = LatLng(animalLatitude!!, animalLongitude!!)
        }

        //Fill views with information
        animalNameTextView.text = animalName
        bionameTextView.text = bioname
        descriptionTextView.text = description
        Glide.with(this).load(animalImgs[0]).into(animalImageView)
        habitatTextView.text = animalHabitat[0]


        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        enableLocation()

        mapboxMap.setStyle(Style.Builder().fromUri("mapbox://styles/brandoniceberg/ck72sen6g1mju1itez0o48916")){style ->

            PermissionsManager.areLocationPermissionsGranted(this).let {
                val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, style).build()

                val locationComponent = mapboxMap.locationComponent

                locationComponent.activateLocationComponent(locationComponentActivationOptions)

                locationComponent.isLocationComponentEnabled = true

                mapBounds()
            }
        }
        //Define map settings
        mapboxMap.apply {
            this.uiSettings.apply {
                this.isCompassEnabled = false
                this.isLogoEnabled = false
                this.isTiltGesturesEnabled = false
                this.isRotateGesturesEnabled = false
                this.attributionGravity = Gravity.BOTTOM
                this.setAttributionMargins(0, 0, 0, 0)
            }
            this.setMaxZoomPreference(18.50)
        }

        desitination = Point.fromLngLat(animalLongitude!!, animalLatitude!!)

        mapboxMap.addMarker(MarkerOptions().position(LatLng(animalLatitude!!, animalLongitude!!)).title("$animalName Exhibit"))

    }

    private fun mapBounds() {
        val desLatLng = LatLng(desitination.latitude(), desitination.longitude())
        val originLatLng = LatLng(originLocation.latitude(), originLocation.longitude())
        val latLngBounds = LatLngBounds.Builder()
            .include(originLatLng)
            .include(desLatLng)
            .build()

        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200), 2500)
    }

    private fun enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            //Do stuff
            initLocationEngine()

        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        locationEngine?.getLastLocation(this)
    }

    override fun onSuccess(result: LocationEngineResult?) {
        val lat = result?.lastLocation!!.latitude
        val lng = result.lastLocation!!.longitude
        originLocation = Point.fromLngLat(lng, lat)
    }

    override fun onFailure(exception: Exception) {
        Log.e(TAG, "onFailure: ${exception.message}")
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "Some features may not be available with location turned off", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted){
            enableLocation()
        }else{
            Toast.makeText(this, "Location permission was denied", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        locationEngine?.removeLocationUpdates(this)
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
