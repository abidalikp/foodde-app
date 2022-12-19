package com.abidali.foodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abidali.foodapp.R
import com.abidali.foodapp.database.CartEntity

class CartRecyclerAdapter(val context: Context, val cartList: List<CartEntity>) :
    RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtItemName: TextView = view.findViewById(R.id.txtNameCart)
        val txtItemPrice: TextView = view.findViewById(R.id.txtCostCart)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart, parent, false)
        return CartViewHolder(view)

    }

    override fun getItemCount(): Int {

        return cartList.size

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val cart = cartList[position]

        holder.txtItemName.text = cart.itemName
        holder.txtItemPrice.text = cart.itemPrice.toString()

    }

}