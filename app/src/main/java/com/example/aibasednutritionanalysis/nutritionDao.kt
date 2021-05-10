package com.example.aibasednutritionanalysis

import androidx.room.*

@Dao
interface nutritionDao {
    @Query("SELECT * FROM nutrition")
    fun getAll(): List<nutrition>

    @Insert
    fun insert(nutrition: nutrition)

    @Query("DELETE FROM nutrition")
    fun deleteAll()

    @Query("SELECT * FROM nutrition WHERE label= :label_num")
    fun getnutrition(label_num: Int):List<nutrition>
}