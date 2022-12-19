package com.abidali.foodapp.activity

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    lateinit var etRegisterName: EditText
    lateinit var etRegisterMail: EditText
    lateinit var etRegisterPhone: EditText
    lateinit var etRegisterAddress: EditText
    lateinit var etRegisterPassword: EditText
    lateinit var etPasswordConfirm: EditText
    lateinit var btnRegister: Button
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreference =
            getSharedPreferences(getString(R.string.registered_preference), Context.MODE_PRIVATE)

        etRegisterName = findViewById(R.id.etRegisterName)
        etRegisterMail = findViewById(R.id.etRegisterMail)
        etRegisterPhone = findViewById(R.id.etRegisterPhone)
        etRegisterAddress = findViewById(R.id.etRegisterAddress)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm)
        btnRegister = findViewById(R.id.btnRegister)


        val queue = Volley.newRequestQueue(this@RegisterActivity)
        val url = "http://13.235.250.119/v2/register/fetch_result"

        btnRegister.setOnClickListener {

            if (ConnectionManager().checkConnectivity((applicationContext))) {

                val jsonParams = JSONObject()
                jsonParams.put("name", etRegisterName.text)
                jsonParams.put("mobile_number", etRegisterPhone.text)
                jsonParams.put("password", etRegisterPassword.text)
                jsonParams.put("address", etRegisterAddress.text)
                jsonParams.put("email", etRegisterMail.text)

                val jsonRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                        try {

                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {

                                val registerJsonObject = data.getJSONObject("data")

                                sharedPreference.edit()
                                    .putString("user_id", registerJsonObject.getString("user_id"))
                                    .apply()
                                sharedPreference.edit()
                                    .putString("name", registerJsonObject.getString("name")).apply()
                                sharedPreference.edit()
                                    .putString("email", registerJsonObject.getString("email"))
                                    .apply()
                                sharedPreference.edit().putString(
                                    "mobile_number",
                                    registerJsonObject.getString("mobile_number")
                                ).apply()
                                sharedPreference.edit()
                                    .putString("address", registerJsonObject.getString("address"))
                                    .apply()
                                sharedPreference.edit().putBoolean("loginSave", true).apply()

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {

                                Toast.makeText(this, "Type Correctly", Toast.LENGTH_SHORT)
                                    .show()

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
