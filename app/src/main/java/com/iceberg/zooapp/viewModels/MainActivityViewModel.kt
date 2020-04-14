package com.iceberg.zooapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.repositories.MainActivityRepo

class MainActivityViewModel : ViewModel() {

    private var animals: MutableLiveData<ArrayList<Animal>> = MutableLiveData()
    private val repo = MainActivityRepo()

    fun init(){
        animals = repo.getAnimals()
    }

    fun getAnimals(): LiveData<ArrayList<Animal>>{
        return animals
    }

}