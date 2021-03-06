package com.iceberg.zooapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.iceberg.zooapp.adpaters.MapAdapter
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.viewModels.ExhibitMapViewModel
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_exhibit_map.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

private const val TAG = "ExhibitMapActivity"

class ExhibitMapActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener, LocationEngineCallback<LocationEngineResult> {

    private lateinit var mapView: MapView
    private lateinit var exhibitName: String
    private lateinit var permissionsManager: PermissionsManager
    private val model = ExhibitMapViewModel()
    private var locationEngine: LocationEngine? = null
    private lateinit var originLocation: Point

    private lateinit var animals: LiveData<ArrayList<Animal>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_exhibit_map)

        checkPermissionStatus()

        //see if we are still loading from the network
        model.isLoading.observe(this, Observer {
            if (it){
                showProgressBar()
            }else {
                hideProgressBar()
                initRecyclerView()
            }
        })

        //Get information from previous activity
        val bundle: Bundle = intent.extras!!
        exhibitName = bundle.getString("exhibit")!!

        animals = model.getAnimals(exhibitName)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        actionBar.title = exhibitName
        actionBar.setDisplayShowHomeEnabled(true)

        map.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        mapView = map
        map.onCreate(savedInstanceState)
        map.getMapAsync(this)
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mapRecyclerView.layoutManager = layoutManager
        mapRecyclerView.adapter = MapAdapter(animals.value!!, WeakReference(this))

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(mapRecyclerView)

        mapRecyclerView.overlay
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.setStyle(Style.Builder().fromUri("mapbox://styles/brandoniceberg/ck72sen6g1mju1itez0o48916")){style ->

            PermissionsManager.areLocationPermissionsGranted(this).let {
                if (it){
                    val locationComponentActivationOptions = LocationComponentActivationOptions.Builder(this, style).locationEngine(locationEngine).build()

                    val locationComponent = mapboxMap.locationComponent

                    locationComponent.activateLocationComponent(locationComponentActivationOptions)

                    locationComponent.isLocationComponentEnabled = true
                }
            }
        }
        mapboxMap.uiSettings.apply {
            this.isCompassEnabled = false
            this.isTiltGesturesEnabled = false
            this.isLogoEnabled = false
            this.isRotateGesturesEnabled = false
            this.setAttributionMargins(50, 0, 0, 500)
        }
        mapboxMap.setMaxZoomPreference(18.50)

        //TODO: Sort the animal list alphabetically
        animals.observe(this, Observer {
            if (it.size > 0) {
                for (animal in animals.value!!) {
                    //If animal has any location data; mark them on the map
                    if (animal.longitude != null || animal.latitude != null){
                        val animalLatitude = animal.latitude
                        val animalLongitude = animal.longitude
                        val animalName = animal.name
                        val animalLocation = LatLng(animalLatitude!!, animalLongitude!!)
                        mapboxMap.addMarker(MarkerOptions().position(animalLocation).title("$animalName Exhibit"))
                    }
                }
            }
        })
    }


    @SuppressLint("LogNotTimber")
    private fun checkPermissionStatus() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initLocationEngine()
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "Some features may not be available with location turned off", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            initLocationEngine()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        locationEngine?.getLastLocation(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSuccess(result: LocationEngineResult?) {
        val lat = result!!.lastLocation!!.latitude
        val lng = result.lastLocation!!.longitude
        originLocation = Point.fromLngLat(lng, lat)
    }

    override fun onFailure(exception: Exception) {
        Log.d(TAG, exception.message!!)
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
