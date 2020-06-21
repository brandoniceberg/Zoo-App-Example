package com.iceberg.zooapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.repositories.AnimalsActivityRepo

class AnimalsActivityViewModel : ViewModel() {

    private var animals: MutableLiveData<ArrayList<Animal>> = MutableLiveData()
    private val repo = AnimalsActivityRepo()

    fun init(){
        animals = repo.getAnimals()
    }

    fun getAnimals(): LiveData<ArrayList<Animal>>{
        return animals
    }

}