package com.example.infiltrados

import android.app.Application
import com.example.infiltrados.backend.Appwrite

class InfiltradosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Appwrite.init(applicationContext)
    }
}