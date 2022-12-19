package com.abidali.foodapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.abidali.foodapp.model.Menu


@Dao
interface ResDao {

    @Insert
    fun insertRes(resEntity: ResEntity)
    @Delete
    fun deleteRes(resEntity: ResEntity)

    @Query("SELECT * FROM restaurants")
    fun getAllRes(): List<ResEntity>

    @Query("SELECT * FROM restaurants WHERE res_id = :resId")
    fun getResById(resId: String): ResEntity


}

@Dao
interface  CartDao {

    @Insert
    fun insertItem(cartEntity: CartEntity)

    @Delete
    fun deleteItem(cartEntity: CartEntity)

    @Query("SELECT * FROM cart_items")
    fun getAllItems(): List<CartEntity>

    @Query("SELECT * FROM cart_items WHERE item_id = :itemId")
    fun getItemById(itemId: String): CartEntity

    @Query("SELECT SUM(item_price) FROM cart_items ")
    fun getTotalPrice(): Int

    @Query("SELECT item_id FROM cart_items")
    fun getItemIds(): List<Int>

    @Query("SELECT COUNT(*) FROM cart_items")
    fun isEmpty(): Int

}