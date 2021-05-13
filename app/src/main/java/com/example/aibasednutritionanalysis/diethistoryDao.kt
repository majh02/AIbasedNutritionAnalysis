package com.example.aibasednutritionanalysis

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface diethistoryDao {
    @Query("SELECT * FROM diethistory")
    fun getAll(): List<diethistory>

    @Insert
    fun insert(diethistory: diethistory)

    @Query("DELETE FROM diethistory")
    fun deleteAll()

    @Query("SELECT * FROM diethistory WHERE id= :label_num")
    fun getdiethistory(label_num: Int):diethistory

    @Query("DELETE FROM diethistory WHERE id= :label_num")
    fun deletediethistory(label_num: Int)

    @Query("SELECT COUNT(*) FROM diethistory")
    fun getCount(): Int
}