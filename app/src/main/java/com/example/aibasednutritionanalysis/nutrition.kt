package com.example.aibasednutritionanalysis

import androidx.room.*

@Entity(tableName = "nutrition")
data class nutrition(@PrimaryKey val label: Int,
                     val food_name: String,
                     val one_time: Int,
                     val energy: Double,
                     val carb: Double,
                     val sugar: Double,
                     val protein: Double,
                     val fat: Double,
                     val fiber: Double,
                     val sodium: Double
)
