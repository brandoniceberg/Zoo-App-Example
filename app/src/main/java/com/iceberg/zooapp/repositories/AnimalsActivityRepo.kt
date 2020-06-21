package com.iceberg.zooapp.repositories

import androidx.lifecycle.MutableLiveData
import com.iceberg.zooapp.R
import com.iceberg.zooapp.models.Animal

class AnimalsActivityRepo {

    private var dataset: ArrayList<Animal> = arrayListOf()


    fun getAnimals(): MutableLiveData<ArrayList<Animal>>{
        setAnimals()

        val data: MutableLiveData<ArrayList<Animal>> = MutableLiveData()
        data.value = dataset

        return data
    }

    private fun setAnimals() {
        dataset.add(Animal(
            "African Painted Dog",
            R.string.african_painted_dog_des,
            "https://tulsazoo.org/wp-content/uploads/2020/04/African-Painted-Dog-Thumbnail-2.jpg",
            R.drawable.meat_icon,
            "Savannahs, woodlands and grasslands",
            null,
            null,
            R.drawable.chimpanzee_status,
            "Lycaon pictus"
        ))

        dataset.add(Animal(
            "African Lion",
            R.string.african_lion_des,
            "https://tulsazoo.org/wp-content/uploads/2015/08/African-Lion-Thumbnail.png",
            R.drawable.meat_icon,
            "Africa",
            36.2092161,
            -95.9083112,
            R.drawable.african_lion_status,
            "Panthera leo"
        ))

        dataset.add(Animal(
            "African Penguin",
            R.string.african_penguin_des,
            "https://tulsazoo.org/wp-content/uploads/2016/03/AfricanPenguin2_NickW_Header-e1581978264547.jpg",
            R.drawable.fish_icon,
            "Africa",
            36.2088699,
            -95.9085846,
            R.drawable.african_penguin_status,
            "Spheniscus demersus"
        ))


        dataset.add(Animal(
            "Aldabra Giant Tortoise",
            R.string.aldabra_tortoise_des,
            "https://tulsazoo.org/wp-content/uploads/2015/07/animal-24.jpg",
            R.drawable.leaf_icon,
            "Aldabra Atoll in Seychelles",
            36.2102394,
            -95.90728,
            R.drawable.aldabra_tortoise_status,
            "Aldabrachelys gigantea"
        ))


        dataset.add(Animal(
            "American Alligator",
            R.string.american_alligator_des,
            "https://tulsazoo.org/wp-content/uploads/2015/08/AmAlligator_NickW_Header.jpg",
            R.drawable.meat_icon,
            "North America",
            36.2122343,
            -95.9073633,
            R.drawable.american_alligator_status,
            "Alligator mississippiensis"
        ))

        dataset.add(Animal(
            "Asian Elephant",
            R.string.asian_elephant_des,
            "https://tulsazoo.org/wp-content/uploads/2015/08/AsianElephant_ThumbnailOnly_TulsaZoo-e1582563115702.png",
            R.drawable.leaf_icon,
            "Asia",
            36.2117988,
            -95.9057745,
            R.drawable.asian_elephant_status,
            "Elephas maximus"
        ))

        dataset.add(Animal(
            "California Sea Lion",
            R.string.california_sea_lion_des,
            "https://tulsazoo.org/wp-content/uploads/2015/08/SeaLion2_NickW_Header-e1582571190669.jpg",
            R.drawable.fish_icon,
            "North America",
            36.2089399,
            -95.9090677,
            R.drawable.california_sea_lion_status,
            "Zalophus californianus"
        ))

        dataset.add(Animal(
            "Chimpanzee",
            R.string.chimp_des,
            "https://tulsazoo.org/wp-content/uploads/2016/03/12657210_10150602858524996_4879366768498657981_o.jpg",
            R.drawable.leaf_icon,
            "Tropical Africa",
            36.2126216,
            -95.9065522,
            R.drawable.chimpanzee_status,
            "Pan troglodytes"
        ))

        dataset.add(Animal(
                "Chinese Alligator",
                R.string.chinese_alligator_des,
                "https://tulsazoo.org/wp-content/uploads/2017/04/Chinese_alligator_Gayle_Tapp-e1492461083945.jpg",
                R.drawable.meat_icon,
                "China",
                null,
                null,
                R.drawable.chinese_alligator_status,
                "Alligator sinensis"
        ))

        dataset.add(Animal(
            "Malayan Tiger",
            R.string.malayan_tiger_des,
            "https://tulsazoo.org/wp-content/uploads/2015/08/Malayan_Tiger.jpg",
            R.drawable.meat_icon,
            "Peninsular Malaysia",
            36.2114182,
            -95.9066925,
            R.drawable.malayan_tiger_status,
            "Panthera tigris jacksoni"
        ))

        dataset.add(Animal(
            "Giraffe",
            R.string.giraffe_des,
            "https://tulsazoo.org/wp-content/uploads/2015/08/Giraffe3_NickW_Header.jpg",
            R.drawable.leaf_icon,
            "Africa",
            36.2092323,
            -95.9065323,
            R.drawable.giraffe_status,
            "Giraffa camelopardalis"
        ))

        dataset.add(Animal(
            "Komodo Dragon",
            R.string.komodo_dragon_des,
            "https://tulsazoo.org/wp-content/uploads/2017/04/Komodo-Dragon-Aaron-Goodwin-1.jpg",
            R.drawable.meat_icon,
            "Indonesian Island of Komodo",
            36.2113744,
            -95.9069635,
            R.drawable.komodo_dragon_status,
            "Varanus komodoensis"
        ))

        dataset.add(Animal(
            "Snow Leopard",
            R.string.snow_leopard_des,
            "https://tulsazoo.org/wp-content/uploads/2016/02/SnowLeopardWeb2.jpg",
            R.drawable.meat_icon,
            "Himalayas",
            36.2110974,
            -95.9069576,
            R.drawable.snow_leopard_status,
            "Unica unica"
        ))

        dataset.add(Animal(
            "White Rhinoceros",
            R.string.white_rhino_des,
            "https://tulsazoo.org/wp-content/uploads/2015/08/IMG_9554b1.jpg",
            R.drawable.leaf_icon,
            "South Africa",
            36.2089341,
            -95.9068628,
            R.drawable.white_rhino_status,
            "Ceratotherium simum"
        ))
    }

}