package com.example.aibasednutritionanalysis

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [diethistory::class], version=1)
@TypeConverters(converters::class)
abstract class diethistoryDB: RoomDatabase() {
    abstract fun diethistoryDao():diethistoryDao
}