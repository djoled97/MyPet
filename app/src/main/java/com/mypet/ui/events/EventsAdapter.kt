//package com.mypet.ui.events
//
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//
//class EventsAdapter(private val dataSet: Array<String>) :
//    RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
//
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//        init {
//            // Define click listener for the ViewHolder's View
//        }
//    }
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(viewGroup.context)
//            .inflate(R.layout.text_row_item, viewGroup, false)
//
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//
//    }
//
//    override fun getItemCount() = dataSet.size
//
//}