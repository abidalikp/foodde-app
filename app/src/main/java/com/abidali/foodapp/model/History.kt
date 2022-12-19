package com.abidali.foodapp.model

data class History(
    val orderId: String,
    val resName: String,
    val totalCost: String,
    val orderTime: String,
    val items: ArrayList<Item>
)
