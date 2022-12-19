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
import androidx.recyclerview.widget.RecyclerView
import com.abidali.foodapp.R
import com.abidali.foodapp.activity.ResMenuActivity
import com.abidali.foodapp.database.ResEntity
import com.abidali.foodapp.fragment.HomeFragment
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context: Context, private val favList: List<ResEntity>) :
    RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgFavResImage: ImageView = view.findViewById(R.id.imgFavResImage)
        val txtFavResName: TextView = view.findViewById(R.id.txtFavResName)
        val txtFavCost: TextView = view.findViewById(R.id.txtFavCost)
        val txtFavRating: TextView = view.findViewById(R.id.txtFavResRating)
        val llFavcontent: LinearLayout = view.findViewById(R.id.llFavContent)
        val imgFavFav: ImageView = view.findViewById(R.id.imgFavFav)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_fav, parent, false)

        return FavouriteViewHolder(view)

    }

    override fun getItemCount(): Int {

        return favList.size

    }


    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        val restaurants = favList[position]


        holder.txtFavResName.text = restaurants.resName
        holder.txtFavRating.text = restaurants.resRating
        holder.txtFavCost.text = restaurants.resCost
        Picasso.get().load(restaurants.image_url).into(holder.imgFavResImage)

        holder.imgFavFav.setOnClickListener {

            if (!HomeFragment.DBAsyncTask(context, restaurants, 1).execute().get()) {

                val async = HomeFragment.DBAsyncTask(context, restaurants, 2).execute()
                val result = async.get()

                if (result) {

                    holder.imgFavFav.setImageResource(R.drawable.ic_fav)

                } else {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

                }
            } else {

                val async = HomeFragment.DBAsyncTask(context, restaurants, 3).execute()
                val result = async.get()

                if (result) {

                    holder.imgFavFav.setImageResource(R.drawable.ic_non_fav)

                } else {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

                }

            }
        }

        holder.llFavcontent.setOnClickListener {

            val intent = Intent(context, ResMenuActivity::class.java)
            intent.putExtra("res_id", restaurants.resId.toString())
            intent.putExtra("res_name", restaurants.resName)
            context.startActivity(intent)

        }

    }
}