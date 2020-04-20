package com.iceberg.zooapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.android.synthetic.main.activity_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class mapActivity: AppCompatActivity(), OnMapReadyCallback, PermissionsListener, LocationEngineCallback<LocationEngineResult>{

    private val TAG = "mapActivity"

    private lateinit var mapView: MapView

    private var animalName: String? = null
    private var animalImage: String? = null
    private var animalHabitat: String? = null
    private var animalDescription: Int? = null
    private var animalLongitude: Double? = null
    private var animalLatitude: Double? = null
    private var animalCoordiates: LatLng? = null
    private var animalStatus: Int? = null
    private var animalBioName: String? = null

    private lateinit var permissionsManager: PermissionsManager

    private lateinit var mapboxMap: MapboxMap
    private var locationEngine: LocationEngine? = null
    private lateinit var originLocation: Point
    private lateinit var desitination : Point

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationEngine = LocationEngineProvider.getBestLocationEngine(this)

        informationCard.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mapView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

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
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        enableLocation()

        mapboxMap.setStyle(Style.Builder().fromUri("mapbox://styles/brandoniceberg/ck72sen6g1mju1itez0o48916")){
            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, it).build()

            val locationComponent = mapboxMap.locationComponent

            locationComponent.activateLocationComponent(locationComponentActivationOptions)

            locationComponent.isLocationComponentEnabled = true
        }
        //Define map settings
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.uiSettings.isTiltGesturesEnabled = false
        mapboxMap.uiSettings.isRotateGesturesEnabled = false
        mapboxMap.uiSettings.isLogoEnabled = false
        mapboxMap.uiSettings.attributionGravity = Gravity.BOTTOM
        mapboxMap.setMaxZoomPreference(18.50)
        mapboxMap.uiSettings.setAttributionMargins(0, 0 , 0 , 0)

        desitination = Point.fromLngLat(animalLongitude!!, animalLatitude!!)

        mapboxMap.addMarker(MarkerOptions().position(LatLng(animalLatitude!!, animalLongitude!!)).title("$animalName Exhibit"))

        NavigationRoute.builder(this)
            .accessToken(getString(R.string.access_token))
            .origin(originLocation)
            .destination(desitination)
            .build()
            .getRoute(object :  Callback<DirectionsResponse> {
                @SuppressLint("LogNotTimber")
                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    Log.d(TAG, "Error: " + t.message)
                    if (!t.message.equals("Coordinate is invalid: 0,0")){
                        Toast.makeText(applicationContext, "Error:  ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<DirectionsResponse>,
                    response: Response<DirectionsResponse>
                ) {
                    if (response.body() == null) {
                        Log.d(TAG,"No routes found, make sure you set the right user and access token.");
                        return
                    } else if (response.body()!!.routes().size < 1) {
                        Toast.makeText(applicationContext, "No routes found", Toast.LENGTH_LONG).show()
                        Log.d(TAG,"No routes found")
                        return
                    }

                }

            })

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

    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        locationEngine?.getLastLocation(this)
    }

    override fun onSuccess(result: LocationEngineResult?) {
        val lat = result!!.lastLocation!!.latitude
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
