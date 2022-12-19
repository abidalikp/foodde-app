package com.abidali.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.abidali.foodapp.R
import com.abidali.foodapp.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    lateinit var etPhoneNumberLogin: EditText
    lateinit var etPasswordLogin: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        title = "Log In"

        sharedPreference =
            getSharedPreferences(getString(R.string.registered_preference), Context.MODE_PRIVATE)

        if (sharedPreference.getBoolean("loginSave", false)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        etPhoneNumberLogin = findViewById(R.id.etPhoneNumberLogin)
        etPasswordLogin = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)

        txtRegister.setOnClickListener {

            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)

        }

        txtForgotPassword.setOnClickListener {

            val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
            startActivity(intent)

        }

        btnLogin.setOnClickListener {


            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/login/fetch_result"

            if (ConnectionManager().checkConnectivity((applicationContext))) {

                val jsonparams = JSONObject()
                jsonparams.put("mobile_number", etPhoneNumberLogin.text)
                jsonparams.put("password", etPasswordLogin.text)


                val jsonRequest =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonparams, Response.Listener {

                            try {

                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {

                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    val loginJsonObject = data.getJSONObject("data")

                                    sharedPreference.edit()
                                        .putString("user_id", loginJsonObject.getString("user_id"))
                                        .apply()
                                    sharedPreference.edit()
                                        .putString("name", loginJsonObject.getString("name"))
                                        .apply()
                                    sharedPreference.edit()
                                        .putString("email", loginJsonObject.getString("email"))
                                        .apply()
                                    sharedPreference.edit().putString(
                                        "mobile_number",
                                        loginJsonObject.getString("mobile_number")
                                    ).apply()
                                    sharedPreference.edit()
                                        .putString("address", loginJsonObject.getString("address"))
                                        .apply()
                                    sharedPreference.edit().putBoolean("loginSave", true).apply()

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
