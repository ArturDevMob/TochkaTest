package com.arturdevmob.tochka.presentation

import android.app.Application
import com.arturdevmob.tochka.di.app.AppComponent
import com.arturdevmob.tochka.di.app.AppModule
import com.arturdevmob.tochka.di.app.DaggerAppComponent

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