package com.abidali.foodapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abidali.foodapp.R
import com.abidali.foodapp.model.History
import com.abidali.foodapp.model.Item
import com.google.gson.JsonArray
import org.json.JSONArray
import org.w3c.dom.Text

class HistoryParentRecyclerAdapter(
    val context: Context,
    val historyList: ArrayList<History>
) :
    RecyclerView.Adapter<HistoryParentRecyclerAdapter.HistoryParentViewHolder>() {

    class HistoryParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtHotelName: TextView = view.findViewById(R.id.txtHotelName)
        val txtDateTime: TextView = view.findViewById(R.id.txtDateTime)
        var recyclerItems: RecyclerView = view.findViewById(R.id.recyclerItems)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryParentViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_history_parent, parent, false)
        return HistoryParentViewHolder(view)

    }

    override fun getItemCount(): Int {

        return historyList.size

    }

    override fun onBindViewHolder(holder: HistoryParentViewHolder, position: Int) {

        val history = historyList[position]

        holder.txtHotelName.text = history.resName
        holder.txtDateTime.text = history.orderTime

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context as Activity)
        val recyclerItemAdapter = HistoryChildRecyclerAdapter(context, history.items)

        holder.recyclerItems.adapter = recyclerItemAdapter
        holder.recyclerItems.layoutManager = layoutManager

        recyclerItemAdapter.notifyDataSetChanged()


    }

}