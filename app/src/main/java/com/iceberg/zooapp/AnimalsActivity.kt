package com.iceberg.zooapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.iceberg.zooapp.adpaters.AnimalListAdapter
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.viewModels.AnimalsActivityViewModel
import kotlinx.android.synthetic.main.activity_animals.*
import java.lang.ref.WeakReference

class AnimalsActivity : AppCompatActivity() {

    private val model = AnimalsActivityViewModel()
    private lateinit var animals: LiveData<ArrayList<Animal>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animals)

        animals = model.animals

        //see if we are still waiting for a response on the network
        model.isLoading.observe(this, Observer {
            if (it){
                showProgressBar()
            }else {
                hideProgressBar()
                initRecyclerView()
            }
        })

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.black_back_arrow)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Animals"

        val view = animalListView
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun initRecyclerView(){
        animalListView.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        animalListView.adapter = AnimalListAdapter(animals.value!!, WeakReference(this))
    }

}
