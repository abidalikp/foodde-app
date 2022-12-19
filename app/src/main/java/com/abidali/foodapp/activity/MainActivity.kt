package com.abidali.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.abidali.foodapp.*
import com.abidali.foodapp.fragment.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frameLayout: FrameLayout
    lateinit var txtNameDrawer: TextView
    lateinit var txtNumberDrawer: TextView
    lateinit var sharedPreference: SharedPreferences

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)


        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        frameLayout = findViewById(R.id.frameLayout)

        val header = navigationView.getHeaderView(0)
        txtNameDrawer = header.findViewById(R.id.txtNameDrawer)
        txtNumberDrawer = header.findViewById(R.id.txtNumberDrawer)

        sharedPreference =
            getSharedPreferences(getString(R.string.registered_preference), Context.MODE_PRIVATE)

        txtNameDrawer.text = sharedPreference.getString("name", "Guest")
        txtNumberDrawer.text = sharedPreference.getString("mobile_number", "9944001122")

        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()

        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {

                R.id.home -> {

                    openHome()
                    drawerLayout.closeDrawers()

                }

                R.id.myProfile -> {

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment()
                        ).commit()

                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()

                }

                R.id.favourites -> {

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavouritesFragment()
                        ).commit()

                    supportActionBar?.title = "Favourite Restaurants"
                    drawerLayout.closeDrawers()

                }

                R.id.orderHistory -> {

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            OrderHistoryFragment()
                        ).commit()

                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawers()

                }

                R.id.faqs -> {

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FaqsFragment()
                        ).commit()

                    supportActionBar?.title = "Frequently Asked Questions"
                    drawerLayout.closeDrawers()

                }

                R.id.logout -> {

                    drawerLayout.closeDrawers()

                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to logout?")
                    dialog.setPositiveButton("Yes") { text, listener ->
                        Toast.makeText(this@MainActivity, "Logged Out", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        sharedPreference.edit().clear().apply()
                        sharedPreference.edit().putBoolean("loginSave", false).apply()
                        applicationContext.deleteDatabase("res-db")
                        startActivity(intent)
                        finish()

                    }
                    dialog.setNegativeButton("No") { text, listener ->
                        openHome()
                    }
                    dialog.create()
                    dialog.show()

                }

            }

            return@setNavigationItemSelectedListener true

        }

    }

    fun setUpToolbar() {

        setSupportActionBar(toolbar)
        supportActionBar?.title = "HOME"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {

            drawerLayout.openDrawer(GravityCompat.START)

        }

        return super.onOptionsItemSelected(item)

    }


    fun openHome() {

        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()

        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)

    }

    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)

        when (frag) {

            !is HomeFragment -> openHome()
            else -> super.onBackPressed()

        }

    }

}
