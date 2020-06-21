package com.iceberg.zooapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.iceberg.zooapp.adpaters.ExhibitRecyclerViewAdapter
import com.iceberg.zooapp.viewModels.ExhibitListViewModel
import kotlinx.android.synthetic.main.activity_exhibit_list.*

class ExhibitList : AppCompatActivity() {

    private val model = ExhibitListViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exhibit_list)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setIcon(R.drawable.tzoo_logo)

        val view = exhibitListView
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        initRecyclerView()
    }

    private fun initRecyclerView() {
        exhibitListView.layoutManager = LinearLayoutManager(this)
        exhibitListView.adapter = ExhibitRecyclerViewAdapter(model.getExhibits().value!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.animal_item -> {
                val intent = Intent(this, AnimalsActivity::class.java)
                startActivity(intent)
            }
            R.id.visit_item -> {
                val intent = Intent(this, PlanATrip::class.java)
                startActivity(intent)
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }
}
