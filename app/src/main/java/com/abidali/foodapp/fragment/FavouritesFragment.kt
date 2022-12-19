package com.abidali.foodapp.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.abidali.foodapp.R
import com.abidali.foodapp.adapter.FavouritesRecyclerAdapter
import com.abidali.foodapp.database.ResDatabase
import com.abidali.foodapp.database.ResEntity


class FavouritesFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouritesRecyclerAdapter
    var dbResList = listOf<ResEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        layoutManager = LinearLayoutManager(activity)

        dbResList = RetrieveFavourites(activity as Context).execute().get()


        if (activity != null) {

            recyclerAdapter = FavouritesRecyclerAdapter(activity as Context, dbResList)

            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager

            recyclerAdapter.notifyDataSetChanged()

        }

        return view

    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<ResEntity>>() {
        override fun doInBackground(vararg params: Void?): List<ResEntity> {

            val db = Room.databaseBuilder(context, ResDatabase::class.java, "res-db").build()

            return db.resDao().getAllRes()

        }

    }

}
