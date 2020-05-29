package com.iceberg.zooapp

import android.annotation.SuppressLint
import android.graphics.Color
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

    private lateinit var directionsFeatureCollection: FeatureCollection

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

        mapboxMap.setStyle(Style.Builder().fromUri("mapbox://styles/brandoniceberg/ck72sen6g1mju1itez0o48916")){
            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, it).build()

            val locationComponent = mapboxMap.locationComponent

            locationComponent.activateLocationComponent(locationComponentActivationOptions)

            locationComponent.isLocationComponentEnabled = true

            initDottedLineSourceAndLayer(it)

            mapBounds()
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

        val client = MapboxDirections.builder()
            .origin(originLocation)
            .destination(desitination)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .accessToken(getString(R.string.access_token))
            .build()

        client.enqueueCall(object : Callback<DirectionsResponse> {
            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                @SuppressLint("LogNotTimber")
                if (response.body() == null) {
                    Log.d(TAG, "No routes found, make sure you set the right user and access token.")
                    return
                } else if (response.body()!!.routes().size < 1) {
                    Log.d(TAG, "No routes found")
                    return
                }
                drawNavigationPolylineRoute(response.body()!!.routes()[0])
            }

        })

    }

    private fun mapBounds() {
        val desLatLng = LatLng(desitination.latitude(), desitination.longitude())
        val originLatLng = LatLng(originLocation.latitude(), originLocation.longitude())
        val latLngBounds = LatLngBounds.Builder()
            .include(originLatLng)
            .include(desLatLng)
            .build()

        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), 5000)
    }

    private fun initDottedLineSourceAndLayer(loadedMapStyle: Style) {
        directionsFeatureCollection = FeatureCollection.fromFeatures(ArrayList<Feature>())

        loadedMapStyle.addSource(GeoJsonSource("SOURCE_ID", directionsFeatureCollection))

        loadedMapStyle.addLayer(
            LineLayer("DIRECTIONS_LAYER_ID", "SOURCE_ID").withProperties(
                lineWidth(4.5f),
                lineColor(Color.BLUE)
            )
        )
    }

    private fun drawNavigationPolylineRoute(route: DirectionsRoute) {
        mapboxMap.getStyle {
            val directionsRouteFeatureList: ArrayList<Feature> = arrayListOf()
            val lineString: LineString = LineString.fromPolyline(route.geometry()!!, PRECISION_6)
            val coordinates: List<Point> = lineString.coordinates()

            for (i in coordinates.indices){
                directionsRouteFeatureList.add(Feature.fromGeometry(LineString.fromLngLats(coordinates)))
            }

            directionsFeatureCollection = FeatureCollection.fromFeatures(directionsRouteFeatureList)
            val source: GeoJsonSource = GeoJsonSource("SOURCE_ID")
            source.setGeoJson(directionsFeatureCollection)

        }
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
