package com.abidali.foodapp.fragment

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abidali.foodapp.R
import com.abidali.foodapp.adapter.HistoryChildRecyclerAdapter
import com.abidali.foodapp.adapter.HistoryParentRecyclerAdapter
import com.abidali.foodapp.adapter.HomeRecyclerAdapter
import com.abidali.foodapp.model.History
import com.abidali.foodapp.model.Item
import com.abidali.foodapp.model.Restaurant
import com.abidali.foodapp.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class OrderHistoryFragment : Fragment() {

    lateinit var recyclerHistory: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerHistoryAdapter: HistoryParentRecyclerAdapter
    lateinit var sharedPreference: SharedPreferences
    lateinit var progressLayoutHistory: RelativeLayout
    lateinit var progressBarHistory: ProgressBar
    val resList = arrayListOf<History>()
    var userId: String? = "122"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        progressBarHistory = view.findViewById(R.id.progressBarHistory)
        progressLayoutHistory = view.findViewById(R.id.progressLayoutHistory)

        recyclerHistory = view.findViewById(R.id.recyclerHistory)
        layoutManager = LinearLayoutManager(activity)

        sharedPreference =
            this.activity!!.getSharedPreferences(
                getString(R.string.registered_preference),
                Context.MODE_PRIVATE
            )
        userId = sharedPreference.getString("user_id", "75")


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/$userId"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")

                    if (success) {

                        progressLayoutHistory.visibility = View.GONE

                        val resArray = data.getJSONArray("data")
                        for (i in 0 until resArray.length()) {

                            val resJsonObject = resArray.getJSONObject(i)

                            val itemList = arrayListOf<Item>()
                            val itemArray = resJsonObject.getJSONArray("food_items")
                            for (j in 0 until itemArray.length()) {

                                val itemJsonObject = itemArray.getJSONObject(j)
                                val itemObject = Item(
                                    itemJsonObject.getString("food_item_id"),
                                    itemJsonObject.getString("name"),
                                    itemJsonObject.getString("cost")
                                )
                                itemList.add(itemObject)

                            }

                            val resObject = History(
                                resJsonObject.getString("order_id"),
                                resJsonObject.getString("restaurant_name"),
                                resJsonObject.getString("total_cost"),
                                resJsonObject.getString("order_placed_at"),
                                itemList
                            )

                            resList.add(resObject)

                            recyclerHistoryAdapter =
                                HistoryParentRecyclerAdapter(activity as Context, resList)

                            if (activity != null) {

                                recyclerHistory.adapter = recyclerHistoryAdapter
                                recyclerHistory.layoutManager = layoutManager

                            }


                            recyclerHistoryAdapter.notifyDataSetChanged()

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

}
