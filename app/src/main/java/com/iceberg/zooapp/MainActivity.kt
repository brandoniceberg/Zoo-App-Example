package com.iceberg.zooapp


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.iceberg.zooapp.adpaters.AnimalListAdapter
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.repositories.MainActivityRepo
import com.iceberg.zooapp.viewModels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private val model = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.init()

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.black_back_arrow)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Animals"

        val view = animalListView
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        initRecyclerView()

    }

    private fun initRecyclerView(){
        animalListView.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        animalListView.adapter = AnimalListAdapter(model.getAnimals().value!!, WeakReference(this))
    }

}
