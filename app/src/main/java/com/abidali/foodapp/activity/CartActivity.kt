package com.abidali.foodapp.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.abidali.foodapp.R
import com.abidali.foodapp.adapter.CartRecyclerAdapter
import com.abidali.foodapp.database.CartDatabase
import com.abidali.foodapp.database.CartEntity
import com.abidali.foodapp.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class CartActivity : AppCompatActivity() {

    lateinit var recyclerCart: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var btnPlaceOrder: Button
    lateinit var txtResName: TextView
    lateinit var coordinatorLayoutCart: CoordinatorLayout
    lateinit var toolbarCart: Toolbar
    lateinit var idList: List<Int>
    lateinit var sharedPreference: SharedPreferences

    var userId: String? = "122"
    var totalCost: Int = 120
    var resId: String? = "100"
    var resName: String? = "Hotel"
    var dbcartList = listOf<CartEntity>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        progressLayout = findViewById(R.id.progressLayoutCart)
        progressBar = findViewById(R.id.progressBarCart)

        recyclerCart = findViewById(R.id.recyclerResCart)
        layoutManager = LinearLayoutManager(applicationContext)

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        txtResName = findViewById(R.id.txtResName)

        coordinatorLayoutCart = findViewById(R.id.coordinatorLayoutCart)
        toolbarCart = findViewById(R.id.toolbarCart)
        setUpToolbar()

        sharedPreference =
            getSharedPreferences(getString(R.string.registered_preference), Context.MODE_PRIVATE)
        userId = sharedPreference.getString("user_id", "75")

        dbcartList = RetrieveCart(applicationContext).execute().get()
        val db = Room.databaseBuilder(applicationContext, CartDatabase::class.java, "cart-db")
            .allowMainThreadQueries().build()
        totalCost = db.cartDao().getTotalPrice()
        idList = db.cartDao().getItemIds()
        db.close()

        if (intent != null) {

            resId = intent.getStringExtra("res_id")
            resName = intent.getStringExtra("res_name")
            txtResName.text = resName

        } else {

            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()

        }

        if (applicationContext != null) {

            progressLayout.visibility = View.GONE
            recyclerAdapter = CartRecyclerAdapter(applicationContext, dbcartList)
            recyclerCart.layoutManager = layoutManager
            recyclerCart.adapter = recyclerAdapter

        }

        btnPlaceOrder.text = "Place Order(Total: Rs. $totalCost)"

        btnPlaceOrder.setOnClickListener {

            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"

            if (ConnectionManager().checkConnectivity((applicationContext))) {

                val jsonArray = JSONArray()
                for (itemId in idList) {
                    jsonArray.put(itemId)
                }

                val jsonParams = JSONObject()
                jsonParams.put("user_id", userId)
                jsonParams.put("restaurant_id", resId)
                jsonParams.put("total_cost", totalCost)
                jsonParams.put("food", jsonArray)

                val jsonRequest =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                            try {

                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {

                                    applicationContext.deleteDatabase("cart-db")
                                    startActivity(
                                        Intent(
                                            this@CartActivity,
                                            SplashOrderActivity::class.java
                                        )
                                    )

                                } else {

                                    Toast.makeText(this, "Unsuccessfull", Toast.LENGTH_SHORT).show()

                                }

                            } catch (e: Exception) {

                                Toast.makeText(this, "Json Exception", Toast.LENGTH_SHORT).show()

                            }
                        }, Response.ErrorListener {

                            Toast.makeText(this, "Volley Error", Toast.LENGTH_SHORT).show()

                        }) {

                        override fun getHeaders(): MutableMap<String, String> {

                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "8cfc33899e3eda"
                            return headers

                        }

                    }

                queue.add(jsonRequest)

            } else {

                Toast.makeText(this, "Internet not Found", Toast.LENGTH_SHORT).show()

            }

        }
    }


    class RetrieveCart(val context: Context) : AsyncTask<Void, Void, List<CartEntity>>() {
        override fun doInBackground(vararg params: Void?): List<CartEntity> {

            val db = Room.databaseBuilder(context, CartDatabase::class.java, "cart-db").build()

            return db.cartDao().getAllItems()

        }

    }


    fun setUpToolbar() {

        setSupportActionBar(toolbarCart)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        super.onBackPressed()
        return true

    }


}

