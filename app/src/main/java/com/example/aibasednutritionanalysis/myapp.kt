package com.example.aibasednutritionanalysis

import android.app.Application

class myapp : Application() {
    companion object{
        lateinit var prefs:MySharedPreferences
    }

    override fun onCreate() {
        prefs= MySharedPreferences(applicationContext)
        super.onCreate()
    }
}