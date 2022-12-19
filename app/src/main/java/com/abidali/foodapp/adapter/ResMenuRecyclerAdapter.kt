package com.abidali.foodapp.adapter

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.abidali.foodapp.R
import com.abidali.foodapp.activity.ResMenuActivity
import com.abidali.foodapp.database.CartDatabase
import com.abidali.foodapp.database.CartEntity
import com.abidali.foodapp.model.Menu
import com.google.gson.Gson
import kotlinx.android.synthetic.main.recycler_res_menu.view.*

class ResMenuRecyclerAdapter(val context: Context, private val itemList: ArrayList<Menu>) :
    RecyclerView.Adapter<ResMenuRecyclerAdapter.ResMenuViewHolder>() {

    class ResMenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtItemNo: TextView = view.findViewById(R.id.txtItemNo)
        val txtItemName: TextView = view.findViewById(R.id.txtItemName)
        val txtItemPrice: TextView = view.findViewById(R.id.txtItemPrice)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResMenuViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_res_menu, parent, false)

        return ResMenuViewHolder(view)

    }

    override fun getItemCount(): Int {

        return itemList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ResMenuViewHolder, position: Int) {

        val menu = itemList[position]

        holder.txtItemName.text = menu.itemName
        holder.txtItemPrice.text = menu.itemCost
        holder.txtItemNo.text = (position + 1).toString()

        val cartEntity = CartEntity(
            menu.itemId.toInt(),
            menu.itemName.toString(),
            menu.itemCost.toInt()
        )

        val checkCart = ResMenuActivity.DBAsyncCart(
            context,
            cartEntity,
            1
        ).execute()
        val isAdded = checkCart.get()

        if (isAdded) {

            holder.btnAdd.setText("Remove")
            val colorRemove = ContextCompat.getColor(context, R.color.colorAccent)
            holder.btnAdd.setBackgroundColor(colorRemove)

        } else {

            holder.btnAdd.setText("Add")
            val colorAdd = ContextCompat.getColor(context, R.color.colorPrimary)
            holder.btnAdd.setBackgroundColor(colorAdd)

        }

        holder.btnAdd.setOnClickListener {

            if (!ResMenuActivity.DBAsyncCart(context, cartEntity, 1).execute().get()) {

                val async = ResMenuActivity.DBAsyncCart(context, cartEntity, 2).execute()
                val result = async.get()

                if (result) {

                    holder.btnAdd.setText("Remove")
                    val colorRemove = ContextCompat.getColor(context, R.color.colorAccent)
                    holder.btnAdd.setBackgroundColor(colorRemove)

                } else {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

                }

            } else {

                val async = ResMenuActivity.DBAsyncCart(context, cartEntity, 3).execute()
                val result = async.get()

                if (result) {

                    holder.btnAdd.setText("Add")
                    val colorAdd = ContextCompat.getColor(context, R.color.colorPrimary)
                    holder.btnAdd.setBackgroundColor(colorAdd)

                } else {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

                }

            }

        }

    }

}

