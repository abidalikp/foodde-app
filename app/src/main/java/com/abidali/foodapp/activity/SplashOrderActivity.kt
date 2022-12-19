package com.abidali.foodapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.abidali.foodapp.R

class SplashOrderActivity : AppCompatActivity() {

    private val sst: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_order)

        Handler().postDelayed({

            startActivity(Intent(this@SplashOrderActivity, MainActivity::class.java))
            finish()

        }, sst)

    }

}
