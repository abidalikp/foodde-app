package com.abidali.foodapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "restaurants")
data class ResEntity (
    @PrimaryKey @ColumnInfo(name = "res_id") val resId: Int,
    @ColumnInfo(name = "res_name") val resName: String,
    @ColumnInfo(name = "res_rating") val resRating: String,
    @ColumnInfo(name = "res_cost") val resCost: String,
    @ColumnInfo(name = "img_url") val image_url: String
)

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey @ColumnInfo(name = "item_id") val itemId: Int,
    @ColumnInfo(name = "item_name") val itemName: String,
    @ColumnInfo(name = "item_price") val itemPrice: Int
)
