package com.example.centerofcat.di

import android.app.Application

class App : Application() {

    companion object {
        val component: AppComponent by lazy { DaggerAppComponent.create() }
    }
}