package com.iceberg.zooapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.iceberg.zooapp.adpaters.ExhibitRecyclerViewAdapter
import com.iceberg.zooapp.classes.Exhibits
import kotlinx.android.synthetic.main.activity_exhibit_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExhibitList : AppCompatActivity() {


    private val listOfExhibits = arrayListOf<Exhibits>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exhibit_list)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setIcon(R.drawable.tzoo_logo)

        if (Build.VERSION.SDK_INT >= 16){
            val view = exhibitListView
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        //Load Exhibits
        CoroutineScope(Dispatchers.Default).launch {
            loadExhibits()
        }

        exhibitListView.layoutManager = LinearLayoutManager(this)
        exhibitListView.adapter = ExhibitRecyclerViewAdapter(listOfExhibits, this)


    }

    private suspend fun loadExhibits() = withContext(Dispatchers.Default) {
        listOfExhibits.add(
            Exhibits(
                "African Plains",
                R.drawable.africa
            )
        )
        listOfExhibits.add(
            Exhibits(
                "Asia",
                R.drawable.asia
            )
        )
        listOfExhibits.add(
            Exhibits(
                "Chimpanzee Connection",
                R.drawable.chimpanzee_connection
            )
        )
        listOfExhibits.add(
            Exhibits(
                "Conservation Center",
                R.drawable.conservation_center
            )
        )
        listOfExhibits.add(
            Exhibits(
                "The Lost Kingdom",
                R.drawable.the_lost_kingdom
            )
        )
        listOfExhibits.add(
            Exhibits(
                "Life in the Cold",
                R.drawable.life_in_the_cold
            )
        )
        listOfExhibits.add(
            Exhibits(
                "Life in the Desert",
                R.drawable.life_in_the_desert
            )
        )
        listOfExhibits.add(
            Exhibits(
                "Life in the Forest",
                R.drawable.life_in_the_forest
            )
        )
        listOfExhibits.add(
            Exhibits(
                "Life in the Water",
                R.drawable.life_in_the_water
            )
        )
        listOfExhibits.add(
            Exhibits(
                "Mary K. Chapman Rhino Reserve",
                R.drawable.mary_k_chapman_rhino_reserve
            )
        )
        listOfExhibits.add(
            Exhibits(
                "Oceans & Islands",
                R.drawable.oceans_and_islands
            )
        )
        listOfExhibits.add(
            Exhibits(
                "The Rainforest",
                R.drawable.the_rainforest
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.animal_item -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.visit_item -> {
                val intent = Intent(this, PlanATrip::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
