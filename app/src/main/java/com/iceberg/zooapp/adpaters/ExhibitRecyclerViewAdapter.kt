package com.iceberg.zooapp.adpaters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iceberg.zooapp.ExhibitMapActivity
import com.iceberg.zooapp.models.Exhibits
import com.iceberg.zooapp.R

class ExhibitRecyclerViewAdapter(private val listOfExhibits: ArrayList<Exhibits>): RecyclerView.Adapter<ExhibitRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.exhibitImageView)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exhibit_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfExhibits.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exhibit = listOfExhibits[position]
        holder.image.setImageResource(exhibit.img)
        holder.cardView.setOnClickListener {
            val intent = Intent(holder.cardView.context, ExhibitMapActivity::class.java)
            intent.putExtra("exhibit", exhibit.name)
            holder.cardView.context.startActivity(intent)
        }
    }
}