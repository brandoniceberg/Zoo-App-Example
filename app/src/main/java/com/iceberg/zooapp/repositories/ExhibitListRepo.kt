package com.iceberg.zooapp.repositories

import androidx.lifecycle.MutableLiveData
import com.iceberg.zooapp.models.Exhibits

class ExhibitListRepo {

    private val dataset: ArrayList<Exhibits> = arrayListOf()


    fun getExhibits(): MutableLiveData<ArrayList<Exhibits>>{
        setExhibits()

        val data: MutableLiveData<ArrayList<Exhibits>> = MutableLiveData()
        data.value = dataset

        return data
    }

    private fun setExhibits() {
        dataset.add(Exhibits(
            "African Plains",
            "https://tulsazoo.org/wp-content/uploads/2015/08/Africa_NickH_Header_Blur.jpg"
        ))

        dataset.add(Exhibits(
            "Conservation Center",
            "https://tulsazoo.org/wp-content/uploads/2015/08/ConservationCenter_NickW_Header.jpg"
        ))

        dataset.add(Exhibits(
            "Chimpanzee Connection",
            "https://tulsazoo.org/wp-content/uploads/2015/08/Chimp1_TulsaZoo_Header.jpg"
        ))

        dataset.add(Exhibits(
            "The Lost Kingdom",
            "https://tulsazoo.org/wp-content/uploads/2015/08/LK_Slides-opening.jpg"
        ))

        dataset.add(Exhibits(
            "Life in the Cold",
            "https://tulsazoo.org/wp-content/uploads/2015/08/LifeInTheCold_NickW_Header.jpg"
        ))

        dataset.add(Exhibits(
            "Life in the Desert",
            "https://tulsazoo.org/wp-content/uploads/2015/08/Desert_Header.jpg"
        ))

        dataset.add(Exhibits(
            "Life in the Forest",
            "https://tulsazoo.org/wp-content/uploads/2015/08/Forest_NickH_Header.jpg"
        ))

        dataset.add(Exhibits(
            "Life in the Water",
            "https://tulsazoo.org/wp-content/uploads/2015/08/Water_TulsaZoo_Header.jpg"
        ))

        dataset.add(Exhibits(
            "Oceans & Islands",
            "https://tulsazoo.org/wp-content/uploads/2015/08/OceansAndIslands_TulsaZoo_Header.jpg"
        ))

        dataset.add(Exhibits(
            "The Rainforest",
            "https://tulsazoo.org/wp-content/uploads/2015/08/Rainforest_TulsaZoo_Header.jpg"
        ))

    }
}