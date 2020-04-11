package com.iceberg.zooapp.adpaters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.iceberg.zooapp.R
import com.iceberg.zooapp.classes.Animal
import com.iceberg.zooapp.mapActivity
import java.lang.ref.WeakReference

class AnimalListAdapter(private val listOfAnimals: ArrayList<Animal>, private val activity: WeakReference<Activity>): RecyclerView.Adapter<AnimalListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val animalName: TextView = itemView.findViewById(R.id.nameTextView)
        val animalImage: ImageView = itemView.findViewById(R.id.animalImageView)
        val animalDescription: TextView = itemView.findViewById(R.id.descriptionTextView)
        val animalCard: CardView = itemView.findViewById(R.id.animal_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.animal_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfAnimals.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal: Animal = listOfAnimals[position]
        holder.animalName.text = animal.name
        holder.animalDescription.text = animal.bioname
        holder.animalImage.setImageResource(animal.image)
        holder.animalCard.setOnClickListener {
            val intent = Intent(holder.animalCard.context, mapActivity::class.java)

            intent.putExtra("name", animal.name)
            intent.putExtra("description", animal.description)
            intent.putExtra("image", animal.image)
            intent.putExtra("habitat", animal.habitat)
            intent.putExtra("food", animal.food)
            intent.putExtra("latitude", animal.latitude)
            intent.putExtra("longitude", animal.longitude)
            intent.putExtra("status", animal.status)
            intent.putExtra("bioname", animal.bioname)
            if (Build.VERSION.SDK_INT >= 21) {
                val options = ActivityOptions.makeSceneTransitionAnimation(activity.get(), holder.animalImage, "animalImage")
                holder.animalCard.context.startActivity(intent, options.toBundle())
            }else {
                holder.animalCard.context.startActivity(intent)
            }
        }
    }
}