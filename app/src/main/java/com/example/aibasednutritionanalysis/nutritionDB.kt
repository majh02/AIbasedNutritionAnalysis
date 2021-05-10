package com.example.aibasednutritionanalysis

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [nutrition::class], version=1)
abstract class nutritionDB: RoomDatabase() {
    abstract fun nutritionDao():nutritionDao
}