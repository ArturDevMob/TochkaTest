package com.example.tochkatest.presentation

import android.app.Application
import com.example.tochkatest.di.app.AppComponent
import com.example.tochkatest.di.app.AppModule
import com.example.tochkatest.di.app.DaggerAppComponent

class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}