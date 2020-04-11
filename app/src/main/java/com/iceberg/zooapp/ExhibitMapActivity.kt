package com.iceberg.zooapp

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.iceberg.zooapp.adpaters.MapAdapter
import com.iceberg.zooapp.classes.Animal
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_exhibit_map.*
import kotlinx.android.synthetic.main.activity_exhibit_map.map
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_map.animalImageView
import kotlinx.android.synthetic.main.exhibitmapcard.*
import kotlinx.coroutines.*
import java.lang.ref.WeakReference

class ExhibitMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "ExhibitMapActivity"

    private lateinit var mapView: MapView
    private val REQUEST_CODE: Int = 5694
    private var animalLatitude: Double? = null
    private var animalLongitude: Double? = null
    private var animalName: String? = null
    private var exhibitName: String? = null

    private var listOfAnimals = ArrayList<Animal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_exhibit_map)

        //Get information from previous activity
        val bundle: Bundle = intent.extras!!
        exhibitName = bundle.getString("exhibit")

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        actionBar.title = exhibitName
        actionBar.setDisplayShowHomeEnabled(true)

        if (Build.VERSION.SDK_INT >= 16){
            map.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        //Load animals
        CoroutineScope(Dispatchers.Default).launch {
            loadAnimals()
        }

        mapView = map
        map.onCreate(savedInstanceState)
        map.getMapAsync(this)

        mapRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)


        mapRecyclerView.adapter = MapAdapter(listOfAnimals, WeakReference(this))

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(mapRecyclerView)


        if (Build.VERSION.SDK_INT >= 18){
            mapRecyclerView.overlay
        }

        checkPermissions()
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

        for (animal in listOfAnimals){
            animalLatitude = animal.latitude
            animalLongitude = animal.longitude
            animalName = animal.name
            val animalLocation = com.mapbox.mapboxsdk.geometry.LatLng(animalLatitude!!, animalLongitude!!)
            mapboxMap.addMarker(MarkerOptions().position(animalLocation).title("$animalName Exhibit"))
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(animalLocation, 18.0))
        }


    }

    private suspend fun loadAnimals() = withContext(Dispatchers.Default) {
        when (exhibitName) {
            "African Plains" -> {
                listOfAnimals.add(
                    Animal(
                        "African Lion",
                        getString(R.string.african_lion_des),
                        R.drawable.african_lion,
                        R.drawable.meat_icon,
                        "Africa",
                        36.2092161,
                        -95.9083112,
                        R.drawable.african_lion_status,
                        "Panthera leo"
                    )
                )
                listOfAnimals.add(
                    Animal(
                        "Aldabra Giant Tortoise",
                        getString(R.string.aldabra_tortoise_des),
                        R.drawable.aldabra_tortoise,
                        R.drawable.leaf_icon,
                        "Aldabra Atoll in Seychelles",
                        36.2102394,
                        -95.90728,
                        R.drawable.aldabra_tortoise_status,
                        "Aldabrachelys gigantea"
                    )
                )
                listOfAnimals.add(
                    Animal(
                        "Giraffe",
                        getString(R.string.giraffe_des),
                        R.drawable.giraffe,
                        R.drawable.leaf_icon,
                        "Africa",
                        36.2092323,
                        -95.9065323,
                        R.drawable.giraffe_status,
                        "Giraffa camelopardalis"
                    )
                )
                listOfAnimals.add(
                    Animal(
                        "White Rhinoceros",
                        getString(R.string.white_rhino_des),
                        R.drawable.white_rhino,
                        R.drawable.leaf_icon,
                        "South Africa",
                        36.2089341,
                        -95.9068628,
                        R.drawable.white_rhino_status,
                        "Ceratotherium simum"
                    )
                )
            }
            "Asia" -> {
                listOfAnimals.add(
                    Animal(
                        "Asian Elephant",
                        getString(R.string.asian_elephant_des),
                        R.drawable.asian_elephant,
                        R.drawable.leaf_icon,
                        "Asia",
                        36.2117988,
                        -95.9057745,
                        R.drawable.asian_elephant_status,
                        "Elephas maximus"
                    )
                )
                listOfAnimals.add(
                    Animal(
                        "Malayan Tiger",
                        getString(R.string.malayan_tiger_des),
                        R.drawable.malayan_tiger,
                        R.drawable.meat_icon,
                        "Peninsular Malaysia",
                        36.2114182,
                        -95.9066925,
                        R.drawable.malayan_tiger_status,
                        "Panthera tigris jacksoni"
                    )
                )
                listOfAnimals.add(
                    Animal(
                        "Snow Leopard",
                        getString(R.string.snow_leopard_des),
                        R.drawable.snow_leopard,
                        R.drawable.meat_icon,
                        "Himalayas",
                        36.2110974,
                        -95.9069576,
                        R.drawable.snow_leopard_status,
                        "Unica unica"
                    )
                )
            }
            "Chimpanzee Connection" -> {
                listOfAnimals.add(
                    Animal(
                        "Chimpanzee",
                        getString(R.string.chimp_des),
                        R.drawable.chimpanzee,
                        R.drawable.leaf_icon,
                        "Tropical Africa",
                        36.2126216,
                        -95.9065522,
                        R.drawable.chimpanzee_status,
                        "Pan troglodytes"
                    )
                )
            }
            "Conservation Center" -> {
                listOfAnimals.add(
                    Animal(
                        "Komodo Dragon",
                        getString(R.string.komodo_dragon_des),
                        R.drawable.komodo_dragon,
                        R.drawable.meat_icon,
                        "Indonesian Island of Komodo",
                        36.2113744,
                        -95.9069635,
                        R.drawable.komodo_dragon_status,
                        "Varanus komodoensis"
                    )
                )
            }
            "The Lost Kingdom" -> {
                listOfAnimals.add(
                    Animal(
                        "Chinese Alligator",
                        getString(R.string.chinese_alligator_des),
                        R.drawable.chinese_alligator,
                        R.drawable.meat_icon,
                        "China",
                        null,
                        null,
                        R.drawable.chinese_alligator_status,
                        "Alligator sinensis"
                    )
                )
                listOfAnimals.add(
                    Animal(
                        "Malayan Tiger",
                        getString(R.string.malayan_tiger_des),
                        R.drawable.malayan_tiger,
                        R.drawable.meat_icon,
                        "Peninsular Malaysia",
                        36.2114182,
                        -95.9066925,
                        R.drawable.malayan_tiger_status,
                        "Panthera tigris jacksoni"
                    )
                )
                listOfAnimals.add(
                    Animal(
                        "Snow Leopard",
                        getString(R.string.snow_leopard_des),
                        R.drawable.snow_leopard,
                        R.drawable.meat_icon,
                        "Himalayas",
                        36.2110974,
                        -95.9069576,
                        R.drawable.snow_leopard_status,
                        "Unica unica"
                    )
                )
            }
            "Life in the Cold" -> {

            }
            "Life in the Desert" -> {

            }
            "Life in the Forest" -> {

            }
            "Life in the Water" -> {
                listOfAnimals.add(
                    Animal(
                        "American Alligator",
                        getString(R.string.american_alligator_des),
                        R.drawable.american_alligator,
                        R.drawable.meat_icon,
                        "North America",
                        36.2122343,
                        -95.9073633,
                        R.drawable.american_alligator_status,
                        "Alligator mississippiensis"
                    )
                )
            }
            "Mary K. Chapman Rhino Reserve" -> {
                listOfAnimals.add(
                    Animal(
                        "White Rhinoceros",
                        getString(R.string.white_rhino_des),
                        R.drawable.white_rhino,
                        R.drawable.leaf_icon,
                        "South Africa",
                        36.2089341,
                        -95.9068628,
                        R.drawable.white_rhino_status,
                        "Ceratotherium simum"
                    )
                )
            }
            "Oceans & Islands" -> {
                listOfAnimals.add(
                    Animal(
                        "African Penguin",
                        getString(R.string.african_penguin_des),
                        R.drawable.african_peguin,
                        R.drawable.fish_icon,
                        "Africa",
                        36.2088699,
                        -95.9085846,
                        R.drawable.african_penguin_status,
                        "Spheniscus demersus"
                    )
                )
                listOfAnimals.add(
                    Animal(
                        "California Sea Lion",
                        getString(R.string.california_sea_lion_des),
                        R.drawable.california_sea_lion,
                        R.drawable.fish_icon,
                        "North America",
                        36.2089399,
                        -95.9090677,
                        R.drawable.california_sea_lion_status,
                        "Zalophus californianus"
                    )
                )
            }
            "The Rainforest" -> {

            }
            else -> {
                Log.d(TAG, "No results found")
            }
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
