package com.iceberg.zooapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iceberg.zooapp.adpaters.ExhibitRecyclerViewAdapter
import com.iceberg.zooapp.models.Exhibits
import com.iceberg.zooapp.viewModels.ExhibitListViewModel
import kotlinx.android.synthetic.main.activity_exhibit_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExhibitList : AppCompatActivity() {

    private val model = ExhibitListViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exhibit_list)

        model.init()

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setIcon(R.drawable.tzoo_logo)

        if (Build.VERSION.SDK_INT >= 16){
            val view = exhibitListView
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

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
