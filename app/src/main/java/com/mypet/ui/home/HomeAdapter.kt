package com.mypet.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypet.databinding.HomeRowItemBinding

class HomeAdapter(private val dataSet: List<Pair<String, String>>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    class ViewHolder(val binding: HomeRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HomeRowItemBinding
                .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            binding.title.text=dataSet[position].first
            binding.body.text=dataSet[position].second
        }

    }

    override fun getItemCount() = dataSet.size

}