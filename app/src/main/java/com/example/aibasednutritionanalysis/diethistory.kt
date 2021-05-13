package com.example.aibasednutritionanalysis

import android.graphics.Bitmap
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*

@Entity(tableName = "diethistory")
data class diethistory(
        @NonNull @PrimaryKey val id: Int,
        val cal: String,
        val meal_time: String,
        val content: String,
        val photo: Bitmap
)

