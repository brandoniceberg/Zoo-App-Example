package com.iceberg.zooapp


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iceberg.zooapp.adpaters.AnimalListAdapter
import com.iceberg.zooapp.classes.Animal
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private var listOfAnimals: ArrayList<Animal> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load animals
        CoroutineScope(Dispatchers.Default).launch {
            loadAnimals()
        }

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.black_back_arrow)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Animals"

        if (Build.VERSION.SDK_INT >= 16) {
            val view = animalListView
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        animalListView.layoutManager = LinearLayoutManager(this)

        animalListView.adapter = AnimalListAdapter(listOfAnimals, WeakReference(this))

    }

    private suspend fun loadAnimals() = withContext(Dispatchers.Default){
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

}
