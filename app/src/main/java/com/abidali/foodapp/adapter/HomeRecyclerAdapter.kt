package com.abidali.foodapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.abidali.foodapp.R
import com.abidali.foodapp.activity.ResMenuActivity
import com.abidali.foodapp.database.ResEntity
import com.abidali.foodapp.fragment.HomeFragment
import com.abidali.foodapp.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, private val itemList: ArrayList<Restaurant>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgResImage: ImageView = view.findViewById(R.id.imgResImage)
        val txtResName: TextView = view.findViewById(R.id.txtResName)
        val txtCost: TextView = view.findViewById(R.id.txtCost)
        val txtRating: TextView = view.findViewById(R.id.txtResRating)
        val llcontent: LinearLayout = view.findViewById(R.id.llcontent)
        val imgFav: ImageView = view.findViewById(R.id.imgFav)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_home, parent, false)

        return HomeViewHolder(view)

    }

    override fun getItemCount(): Int {

        return itemList.size

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val restaurant = itemList[position]

        holder.txtResName.text = restaurant.resName
        holder.txtRating.text = restaurant.resRating
        holder.txtCost.text = restaurant.resCost
        Picasso.get().load(restaurant.image_url).into(holder.imgResImage)

        val resEntity = ResEntity(
            restaurant.resId.toInt(),
            restaurant.resName.toString(),
            restaurant.resRating.toString(),
            restaurant.resCost.toString(),
            restaurant.image_url
        )


        val checkFav = HomeFragment.DBAsyncTask(context, resEntity, 1).execute()
        val isFav = checkFav.get()

        if (isFav) {

            holder.imgFav.setBackgroundResource(R.drawable.ic_fav)

        } else {

            holder.imgFav.setBackgroundResource(R.drawable.ic_non_fav)

        }

        holder.imgFav.setOnClickListener {

            if (!HomeFragment.DBAsyncTask(context, resEntity, 1).execute().get()) {

                val async = HomeFragment.DBAsyncTask(context, resEntity, 2).execute()
                val result = async.get()

                if (result) {

                    holder.imgFav.setBackgroundResource(R.drawable.ic_fav)

                } else {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

                }

            } else {

                val async = HomeFragment.DBAsyncTask(context, resEntity, 3).execute()
                val result = async.get()

                if (result) {

                    holder.imgFav.setBackgroundResource(R.drawable.ic_non_fav)

                } else {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

                }

            }

        }

        holder.llcontent.setOnClickListener {

            val intent = Intent(context, ResMenuActivity::class.java)
            intent.putExtra("res_id", restaurant.resId)
            intent.putExtra("res_name", restaurant.resName)
            context.startActivity(intent)

        }


    }
}