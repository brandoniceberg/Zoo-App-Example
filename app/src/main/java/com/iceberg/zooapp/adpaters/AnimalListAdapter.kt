package com.iceberg.zooapp.adpaters

import android.app.Activity
import android.util.Pair as UtilPair
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iceberg.zooapp.R
import com.iceberg.zooapp.MapActivity
import com.iceberg.zooapp.models.Animal
import java.lang.ref.WeakReference

class AnimalListAdapter(private val listOfAnimals: ArrayList<Animal>, private val activity: WeakReference<Activity>): RecyclerView.Adapter<AnimalListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val animalName: TextView = itemView.findViewById(R.id.animalNameTextView)
        val animalImage: ImageView = itemView.findViewById(R.id.animalImageView)
        val animalCard: CardView = itemView.findViewById(R.id.animal_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.animal_grid_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfAnimals.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal: Animal = listOfAnimals[position]
        holder.animalName.text = animal.name
        Glide.with(holder.animalImage.context).load(animal.imgs[0]).into(holder.animalImage)
        holder.animalCard.setOnClickListener {
            val intent = Intent(holder.animalCard.context, MapActivity::class.java)

            intent.apply {
                this.putExtra("name", animal.name)
                this.putExtra("bioname", animal.bioname)
                this.putExtra("description", animal.description)
                this.putExtra("imgs", animal.imgs)
                this.putExtra("habitat", animal.habitat)
                this.putExtra("latitude", animal.latitude)
                this.putExtra("longitude", animal.longitude)
            }
            if (Build.VERSION.SDK_INT >= 21) {
                val options = ActivityOptions.makeSceneTransitionAnimation(activity.get(),
                    UtilPair.create(holder.animalImage, "animalImage"),
                    UtilPair.create(holder.animalName, "animalName"),
                    UtilPair.create(holder.animalCard, "animalCard"))
                holder.animalCard.context.startActivity(intent, options.toBundle())
            }else {
                holder.animalCard.context.startActivity(intent)
            }
        }
    }
}