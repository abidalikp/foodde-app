package com.abidali.foodapp.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [ResEntity::class], version = 1)
abstract class ResDatabase: RoomDatabase() {

    abstract fun resDao(): ResDao
}

@Database(entities = [CartEntity::class], version = 1)
abstract class CartDatabase: RoomDatabase() {

    abstract fun cartDao(): CartDao

}

