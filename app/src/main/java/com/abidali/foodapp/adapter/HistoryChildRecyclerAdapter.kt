package com.abidali.foodapp.adapter

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

class HistoryChildRecyclerAdapter(val context: Context, val itemList: ArrayList<Item>) :
    RecyclerView.Adapter<HistoryChildRecyclerAdapter.HistoryChildViewHolder>() {

    class HistoryChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtHistoryItem: TextView = view.findViewById(R.id.txtHistoryItem)
        val txtHistoryCost: TextView = view.findViewById(R.id.txtHistoryCost)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryChildViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_history_child, parent, false)
        return HistoryChildViewHolder(view)

    }

    override fun getItemCount(): Int {

        return itemList.size

    }

    override fun onBindViewHolder(holder: HistoryChildViewHolder, position: Int) {

        val item = itemList[position]

        holder.txtHistoryItem.text = item.itemName
        holder.txtHistoryCost.text = item.itemCost

    }

}