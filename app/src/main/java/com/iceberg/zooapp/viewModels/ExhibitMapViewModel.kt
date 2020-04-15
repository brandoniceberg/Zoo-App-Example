package com.iceberg.zooapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.repositories.ExhibitMapRepo

class ExhibitMapViewModel: ViewModel() {

    private var animals: MutableLiveData<ArrayList<Animal>> = MutableLiveData()
    private val repo = ExhibitMapRepo()


    fun init(exhibit: String) {
        animals = repo.getAnimals(exhibit)
    }

    fun getAnimals(): LiveData<ArrayList<Animal>> {
        return animals
    }
}