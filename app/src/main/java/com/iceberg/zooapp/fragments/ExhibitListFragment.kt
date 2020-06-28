package com.iceberg.zooapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iceberg.zooapp.R
import com.iceberg.zooapp.adpaters.ExhibitRecyclerViewAdapter
import com.iceberg.zooapp.models.Exhibits
import com.iceberg.zooapp.viewModels.ExhibitListViewModel
import kotlinx.android.synthetic.main.exhibit_list_fragment.*

private const val TAG = "ExhibitListFragment"

class ExhibitListFragment: Fragment(R.layout.exhibit_list_fragment) {

    private val exhibits: ArrayList<Exhibits> = ExhibitListViewModel().exhibits.value!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }


    private fun initRecyclerView() {
        exhibitRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        exhibitRecyclerView.adapter = ExhibitRecyclerViewAdapter(exhibits)
    }
}