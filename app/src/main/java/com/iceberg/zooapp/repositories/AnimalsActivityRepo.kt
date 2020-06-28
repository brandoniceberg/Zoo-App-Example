package com.iceberg.zooapp.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.iceberg.zooapp.models.Animal
import kotlinx.coroutines.*

private const val TAG = "AnimalsActivityRepo"

class AnimalsActivityRepo {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val data: MutableLiveData<ArrayList<Animal>> = MutableLiveData()

    private val listOfAnimals: ArrayList<Animal> = arrayListOf()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)


    @SuppressLint("LogNotTimber")
    fun getAnimals(): MutableLiveData<ArrayList<Animal>> {

        try {
            isLoading.value = true
            fetchData()
        }catch (e: Exception){
            Log.d(TAG, "Exception: ${e.message}")
        }

        return data
    }


    private fun fetchData() {
        val task = database.collection("animals").get()

        task.addOnSuccessListener {
            for (document in it.documents){
                val animal: Animal = document.toObject(Animal::class.java)!!
                listOfAnimals.add(animal)
            }
            data.value = listOfAnimals
            isLoading.value = false
        }.addOnFailureListener {
            Log.e(TAG, "Failed with: ${it.message}")
        }
    }

}