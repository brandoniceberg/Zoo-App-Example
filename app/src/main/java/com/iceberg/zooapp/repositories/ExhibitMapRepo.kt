package com.iceberg.zooapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.iceberg.zooapp.models.Animal
import com.iceberg.zooapp.models.ExhibitData
import kotlinx.coroutines.*
import kotlin.math.log

private const val TAG = "ExhibitMapRepo"

class ExhibitMapRepo {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val data: MutableLiveData<ArrayList<Animal>> = MutableLiveData()

    private val listOfAnimals: ArrayList<Animal> = arrayListOf()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getAnimals(exhibit: String): MutableLiveData<ArrayList<Animal>> {
        try {
            isLoading.value = true
            fetchData(exhibit)
        }catch (e: Exception){
            Log.d(TAG, "Exception: ${e.message}")
        }


        return data
    }


    private fun fetchData(exhibit: String) {
        val task = database.collection("animals").whereEqualTo(exhibit, true).get()

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