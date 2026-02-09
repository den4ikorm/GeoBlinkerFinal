package org.example.geoblinker

import android.app.Application
import android.util.Log

class GeoBlinkerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("GeoBlinkerApp", "Application started successfully")
        
        // TODO: Инициализация Koin DI
        // TODO: Инициализация SQLDelight
        // TODO: Инициализация Network client
    }
}
