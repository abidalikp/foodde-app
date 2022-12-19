package com.abidali.foodapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.abidali.foodapp.R
import com.abidali.foodapp.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Forgot2Activity : AppCompatActivity() {

    lateinit var etOTPForgot: EditText
    lateinit var etPasswordForgot: EditText
    lateinit var etConfirmForgot: EditText
    lateinit var btnSubmitForgot: Button
    var number: String? = "999"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot2)

        etOTPForgot = findViewById(R.id.etOTPForgot)
        etPasswordForgot = findViewById(R.id.etPasswordForgot)
        etConfirmForgot = findViewById(R.id.etConfirmForgot)
        btnSubmitForgot = findViewById(R.id.btnSubmitForgot)


        if (intent != null) {

            number = intent.getStringExtra("mobile_number")

        } else {

            Toast.makeText(this, "Error Inn", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (number == "999") {

            Toast.makeText(this, "Error 999", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        btnSubmitForgot.setOnClickListener {

            if (etPasswordForgot.text.toString() != etConfirmForgot.text.toString()) {

                Toast.makeText(this, "Passwords doesn't match", Toast.LENGTH_SHORT).show()

            } else {

                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"

                if (ConnectionManager().checkConnectivity((applicationContext))) {

                    val jsonParams = JSONObject()
                    jsonParams.put("password", etPasswordForgot.text)
                    jsonParams.put("otp", etOTPForgot.text)
                    jsonParams.put("mobile_number", number.toString())

                    val jsonRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                            try {

                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")

                                if (success) {

                                    val intent = Intent(this, LoginActivity::class.java)

                                    Toast.makeText(
                                        this,
                                        data.getString("successMessage"),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    startActivity(intent)
                                    finish()

                                } else {

                                    Toast.makeText(
                                        this,
                                        data.getString("Type Correctly"),
                                        Toast.LENGTH_SHORT
                                    ).show()

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

    }
}
