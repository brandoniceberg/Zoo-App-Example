package com.iceberg.zooapp.repositories

import androidx.lifecycle.MutableLiveData
import com.iceberg.zooapp.R
import com.iceberg.zooapp.models.Exhibits

class ExhibitListRepo {

    private val dataset: ArrayList<Exhibits> = arrayListOf()


    fun getExhibits(): MutableLiveData<ArrayList<Exhibits>>{

        val data: MutableLiveData<ArrayList<Exhibits>> = MutableLiveData()
        data.value = setExhibits()

        return data
    }

    private fun setExhibits(): ArrayList<Exhibits> {
        dataset.add(Exhibits(
            "Africa",
            R.drawable.africa
        ))

        dataset.add(Exhibits(
            "Chimpanzee Connection",
            R.drawable.chimpanzee_connection
        ))

        dataset.add(Exhibits(
            "Conservation Center",
            R.drawable.conservation_center
        ))

        dataset.add(Exhibits(
            "Lost Kingdom",
            R.drawable.the_lost_kingdom
        ))

        dataset.add(Exhibits(
            "Life in the Cold",
            R.drawable.life_in_the_cold
        ))

        dataset.add(Exhibits(
            "Life in the Desert",
            R.drawable.life_in_the_desert
        ))

        dataset.add(Exhibits(
            "Life in the Forest",
            R.drawable.life_in_the_forest
        ))

        dataset.add(Exhibits(
            "Life in the Water",
            R.drawable.life_in_the_water
        ))

        dataset.add(Exhibits(
            "Oceans and Islands",
            R.drawable.oceans_and_islands
        ))

        dataset.add(Exhibits(
            "The Rainforest",
            R.drawable.the_rainforest
        ))
        return dataset
    }
}