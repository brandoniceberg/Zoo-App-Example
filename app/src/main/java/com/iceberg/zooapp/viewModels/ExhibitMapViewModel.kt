package com.iceberg.zooapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.repositories.ExhibitMapRepo

private const val TAG = "ExhibitMapViewModel"

class ExhibitMapViewModel: ViewModel() {

    private val repo = ExhibitMapRepo()

    val isLoading: LiveData<Boolean> = repo.isLoading

    fun getAnimals(exhibit: String): LiveData<ArrayList<Animal>> {
        return repo.getAnimals(exhibit)
    }
}