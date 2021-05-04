package com.example.centerofcat.di

import com.example.centerofcat.data.api.ApiService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiFactoryModule::class, ApiServiceModule::class])
interface AppComponent {
    fun provideApi(): ApiService
}