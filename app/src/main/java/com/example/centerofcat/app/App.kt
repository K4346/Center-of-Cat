package com.example.centerofcat.app

import android.app.Application
import com.example.centerofcat.app.di.AppComponent
import com.example.centerofcat.app.di.DaggerAppComponent

class App : Application() {
        val component: AppComponent by lazy { DaggerAppComponent.create() }
}