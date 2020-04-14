package com.iceberg.zooapp


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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

        if (Build.VERSION.SDK_INT >= 16) {
            val view = animalListView
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        initRecyclerView()

    }

    private fun initRecyclerView(){
        animalListView.layoutManager = LinearLayoutManager(this)
        animalListView.adapter = AnimalListAdapter(model.getAnimals().value!!, WeakReference(this))
    }

}
