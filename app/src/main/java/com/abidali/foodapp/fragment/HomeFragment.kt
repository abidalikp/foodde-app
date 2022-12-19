package com.abidali.foodapp.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.abidali.foodapp.R
import com.abidali.foodapp.adapter.HomeRecyclerAdapter
import com.abidali.foodapp.database.ResDatabase
import com.abidali.foodapp.database.ResEntity
import com.abidali.foodapp.model.Restaurant
import com.abidali.foodapp.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayoutHome: RelativeLayout
    lateinit var progressBarHome: ProgressBar
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    val resList = arrayListOf<Restaurant>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        progressBarHome = view.findViewById(R.id.progressBarHome)
        progressLayoutHome = view.findViewById(R.id.progressLayoutHome)

        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")

                    if (success) {

                        progressLayoutHome.visibility = View.GONE

                        val resArray = data.getJSONArray("data")
                        for (i in 0 until resArray.length()) {
                            val resJsonObject = resArray.getJSONObject(i)
                            val resObject = Restaurant(
                                resJsonObject.getString("id"),
                                resJsonObject.getString("name"),
                                resJsonObject.getString("rating"),
                                resJsonObject.getString("cost_for_one"),
                                resJsonObject.getString("image_url")
                            )
                            resList.add(resObject)

                            if (activity != null) {

                                recyclerAdapter = HomeRecyclerAdapter(activity as Context, resList)
                                recyclerHome.adapter = recyclerAdapter
                                recyclerHome.layoutManager = layoutManager

                            }

                        }

                    } else {

                        Toast.makeText(
                            activity as Context,
                            "Some error occurred!",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }

                }, Response.ErrorListener {

                    if (activity != null) {

                        Toast.makeText(activity as Context, "ERROR", Toast.LENGTH_SHORT).show()

                    }

                }) {

                    override fun getHeaders(): MutableMap<String, String> {

                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "8cfc33899e3eda"
                        return headers

                    }

                }

            queue.add(jsonObjectRequest)

        } else {

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection Found!\nPlease connect to the Internet and re-open the app.")
            dialog.setPositiveButton("Ok") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }

        return view

    }

    class DBAsyncTask(val context: Context, val resEntity: ResEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        private val db = Room.databaseBuilder(context, ResDatabase::class.java, "res-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {

                    val res: ResEntity? = db.resDao().getResById(resEntity.resId.toString())
                    db.close()
                    return res != null

                }

                2 -> {

                    db.resDao().insertRes(resEntity)
                    db.close()
                    return true

                }

                3 -> {

                    db.resDao().deleteRes(resEntity)
                    db.close()
                    return true

                }

            }

            return false

        }

    }

}
