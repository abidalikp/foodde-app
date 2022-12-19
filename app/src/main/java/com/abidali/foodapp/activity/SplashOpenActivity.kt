package com.abidali.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.abidali.foodapp.R

class SplashOpenActivity : AppCompatActivity() {

    private val sst: Long = 2000
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_open)

        sharedPreference =
            getSharedPreferences(getString(R.string.registered_preference), Context.MODE_PRIVATE)

        Handler().postDelayed({

            if (sharedPreference.getBoolean("loginSave", false)) {

                startActivity(Intent(this, MainActivity::class.java))
                finish()

            } else {

                startActivity(Intent(this, LoginActivity::class.java))
                finish()

            }

        }, sst)

    }

}
