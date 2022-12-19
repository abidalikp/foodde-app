package com.abidali.foodapp.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.abidali.foodapp.R
import com.abidali.foodapp.adapter.ResMenuRecyclerAdapter
import com.abidali.foodapp.database.CartDatabase
import com.abidali.foodapp.database.CartEntity
import com.abidali.foodapp.model.Menu
import com.abidali.foodapp.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class ResMenuActivity : AppCompatActivity() {


    lateinit var recyclerResMenu: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: ResMenuRecyclerAdapter
    lateinit var progressLayoutMenu: RelativeLayout
    lateinit var progressBarMenu: ProgressBar
    lateinit var btnProceedCart: Button
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    var menuList = arrayListOf<Menu>()
    var resId: String? = "100"
    var resName: String? = "Hotel"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_menu)

        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()

        progressBarMenu = findViewById(R.id.progressBarMenu)
        progressLayoutMenu = findViewById(R.id.progressLayoutMenu)
        btnProceedCart = findViewById(R.id.btnProceedCart)
        recyclerResMenu = findViewById(R.id.recyclerResMenu)
        layoutManager = LinearLayoutManager(this)

        if (intent != null) {

            resId = intent.getStringExtra("res_id")
            resName = intent.getStringExtra("res_name")
            supportActionBar?.title = resName

        } else {

            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()

        }

        if (resId == "100") {

            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            finish()

        }

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"

        if (ConnectionManager().checkConnectivity((applicationContext))) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")

                    if (success) {

                        progressLayoutMenu.visibility = View.GONE

                        val menuArray = data.getJSONArray("data")
                        for (i in 0 until menuArray.length()) {

                            val menuJsonObject = menuArray.getJSONObject(i)
                            val menuObject = Menu(
                                menuJsonObject.getString("id"),
                                menuJsonObject.getString("name"),
                                menuJsonObject.getString("cost_for_one"),
                                menuJsonObject.getString("restaurant_id")
                            )
                            menuList.add(menuObject)

                            if (applicationContext != null) {

                                recyclerAdapter =
                                    ResMenuRecyclerAdapter(this as Context, menuList)
                                recyclerResMenu.adapter = recyclerAdapter
                                recyclerResMenu.layoutManager = layoutManager

                            }

                        }

                    }

                }, Response.ErrorListener {

                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()

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

            Toast.makeText(this, "Internet not Found", Toast.LENGTH_SHORT).show()

        }


        btnProceedCart.setOnClickListener {

            val db = Room.databaseBuilder(applicationContext, CartDatabase::class.java, "cart-db")
                .allowMainThreadQueries().build()
            val count = db.cartDao().isEmpty()
            db.close()

            if (count > 0) {

                val intent = Intent(this, CartActivity::class.java)
                intent.putExtra("res_id", resId)
                intent.putExtra("res_name", resName)
                startActivity(intent)

            } else {

                Toast.makeText(this, "Cart is Empty", Toast.LENGTH_SHORT).show()

            }


        }

    }

    fun setUpToolbar() {

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Hotel"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    class DBAsyncCart(val context: Context, val cartEntity: CartEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, CartDatabase::class.java, "cart-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
                    val item: CartEntity? =
                        db.cartDao().getItemById(cartEntity.itemId.toString())
                    db.close()
                    return item != null
                }

                2 -> {
                    db.cartDao().insertItem(cartEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.cartDao().deleteItem(cartEntity)
                    db.close()
                    return true
                }

                4 -> {
                    val count = db.cartDao().isEmpty()
                    db.close()
                    return count > 0
                }


            }

            return false

        }

    }

    override fun onBackPressed() {

        applicationContext.deleteDatabase("cart-db")
        super.onBackPressed()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        applicationContext.deleteDatabase("cart-db")
        super.onBackPressed()
        return true

    }
}
