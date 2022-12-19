package com.abidali.foodapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abidali.foodapp.R
import android.content.SharedPreferences
import android.widget.TextView


class ProfileFragment : Fragment() {

    lateinit var sharedPreference: SharedPreferences
    lateinit var txtProfileName: TextView
    lateinit var txtProfilePhone: TextView
    lateinit var txtProfileMail: TextView
    lateinit var txtProfileAddress: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        txtProfileName = view.findViewById(R.id.txtProfileName)
        txtProfilePhone = view.findViewById(R.id.txtProfilePhone)
        txtProfileAddress = view.findViewById(R.id.txtProfileAddress)
        txtProfileMail = view.findViewById(R.id.txtProfileMail)

        sharedPreference =
            this.activity!!.getSharedPreferences(
                getString(R.string.registered_preference),
                Context.MODE_PRIVATE
            )

        txtProfileName.text = sharedPreference.getString("name", "Name")
        txtProfilePhone.text = sharedPreference.getString("mobile_number", "Mobile Number")
        txtProfileAddress.text = sharedPreference.getString("address", "Delivery Address")
        txtProfileMail.text = sharedPreference.getString("email", "Email Address")

        return view

    }

}
