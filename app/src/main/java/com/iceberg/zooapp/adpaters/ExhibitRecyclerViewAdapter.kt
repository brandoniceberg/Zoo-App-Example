package com.iceberg.zooapp.adpaters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.iceberg.zooapp.ExhibitMapActivity
import com.iceberg.zooapp.classes.Exhibits
import com.iceberg.zooapp.R

class ExhibitRecyclerViewAdapter(private val listOfExhibits: ArrayList<Exhibits>, val context: Context): RecyclerView.Adapter<ExhibitRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.exhibitImageView)
        val parentLayout: LinearLayout = itemView.findViewById(R.id.exhibitListParentLayout)
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
        holder.parentLayout.setOnClickListener {
            val intent = Intent(context, ExhibitMapActivity::class.java)
            intent.putExtra("exhibit", exhibit.name)
            context.startActivity(intent)
        }
    }
}