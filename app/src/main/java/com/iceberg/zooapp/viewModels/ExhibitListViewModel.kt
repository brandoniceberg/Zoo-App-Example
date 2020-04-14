package com.iceberg.zooapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iceberg.zooapp.models.Exhibits
import com.iceberg.zooapp.repositories.ExhibitListRepo

class ExhibitListViewModel: ViewModel() {

    private var exhibits: MutableLiveData<ArrayList<Exhibits>> = MutableLiveData()
    private val repo = ExhibitListRepo()

    fun init() {
        exhibits = repo.getExhibits()
    }

    fun getExhibits(): LiveData<ArrayList<Exhibits>>{
        return exhibits
    }

}