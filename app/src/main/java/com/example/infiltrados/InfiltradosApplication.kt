package com.example.infiltrados

import android.app.Application
import com.example.infiltrados.services.AppwriteService

class InfiltradosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppwriteService.init(applicationContext)
    }
}