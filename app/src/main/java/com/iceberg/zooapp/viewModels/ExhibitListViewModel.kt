package com.iceberg.zooapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iceberg.zooapp.models.Exhibits
import com.iceberg.zooapp.repositories.ExhibitListRepo

class ExhibitListViewModel: ViewModel() {

    val exhibits: LiveData<ArrayList<Exhibits>> = ExhibitListRepo().getExhibits()

}