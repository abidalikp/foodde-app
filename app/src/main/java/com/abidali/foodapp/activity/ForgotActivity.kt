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

class ForgotActivity : AppCompatActivity() {

    lateinit var etPhoneNumberForgot: EditText
    lateinit var etEmailForgot: EditText
    lateinit var btnForgotNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        title = "Change Password"

        etPhoneNumberForgot = findViewById(R.id.etPhoneNumberForgot)
        etEmailForgot = findViewById(R.id.etEmailForgot)
        btnForgotNext = findViewById(R.id.btnForgotNext)

        btnForgotNext.setOnClickListener {

            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

            if (ConnectionManager().checkConnectivity((applicationContext))) {

                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", etPhoneNumberForgot.text)
                jsonParams.put("email", etEmailForgot.text)

                val jsonRequest =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                            try {

                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                val first = data.getBoolean("first_try")

                                if (!first) {

                                    Toast.makeText(this, "Use previous OTP", Toast.LENGTH_LONG)
                                        .show()

                                }

                                if (success) {

                                    val intent = Intent(this, Forgot2Activity::class.java)
                                    intent.putExtra(
                                        "mobile_number",
                                        etPhoneNumberForgot.text.toString()
                                    )
                                    startActivity(intent)
                                    finish()

                                } else {

                                    Toast.makeText(this, "Type Correctly", Toast.LENGTH_SHORT).show()

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

