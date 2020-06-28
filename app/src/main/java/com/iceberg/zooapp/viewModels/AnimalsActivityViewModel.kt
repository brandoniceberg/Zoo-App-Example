package com.iceberg.zooapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.repositories.AnimalsActivityRepo

class AnimalsActivityViewModel : ViewModel() {

    private val repo = AnimalsActivityRepo()

    val isLoading: LiveData<Boolean> = repo.isLoading

    val animals: LiveData<ArrayList<Animal>> = repo.getAnimals()

}