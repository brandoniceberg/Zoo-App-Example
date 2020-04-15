package com.iceberg.zooapp

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.iceberg.zooapp.adpaters.MapAdapter
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.viewModels.ExhibitMapViewModel
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_exhibit_map.*
import kotlinx.android.synthetic.main.activity_exhibit_map.map
import kotlinx.coroutines.*
import java.lang.ref.WeakReference

class ExhibitMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "ExhibitMapActivity"

    private lateinit var mapView: MapView
    private val REQUEST_CODE: Int = 5694
    private var exhibitName: String? = null
    private val model = ExhibitMapViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_exhibit_map)

        //Get information from previous activity
        val bundle: Bundle = intent.extras!!
        exhibitName = bundle.getString("exhibit")

        model.init(exhibitName!!)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        actionBar.title = exhibitName
        actionBar.setDisplayShowHomeEnabled(true)

        map.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE


        mapView = map
        map.onCreate(savedInstanceState)
        map.getMapAsync(this)

        initRecyclerView()

        checkPermissions()
    }

    private fun initRecyclerView(){
        mapRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mapRecyclerView.adapter = MapAdapter(model.getAnimals().value!!, WeakReference(this))

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(mapRecyclerView)


        mapRecyclerView.overlay
    }

    private fun checkPermissions(){
        if (Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
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
                    Log.d("LOCATION ACCESS", "Location access granted.")
                } else {
                    Log.d("LOCATION ACCESS", "Location access was denied.")
                }
            }
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.setStyle(Style.Builder().fromUri("mapbox://styles/brandoniceberg/ck72sen6g1mju1itez0o48916"))
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.uiSettings.isTiltGesturesEnabled = false
        mapboxMap.uiSettings.isRotateGesturesEnabled = false
        mapboxMap.uiSettings.isLogoEnabled = false
        mapboxMap.setMaxZoomPreference(18.50)
        mapboxMap.setMinZoomPreference(15.60)
        mapboxMap.uiSettings.setAttributionMargins(50, 0 , 0 , 500)

        for (animal in model.getAnimals().value!!){
            val animalLatitude = animal.latitude
            val animalLongitude = animal.longitude
            val animalName = animal.name
            val animalLocation = com.mapbox.mapboxsdk.geometry.LatLng(animalLatitude!!, animalLongitude!!)
            mapboxMap.addMarker(MarkerOptions().position(animalLocation).title("$animalName Exhibit"))
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(animalLocation, 18.0))
        }


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
