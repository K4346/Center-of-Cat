package com.example.centerofcat.app.di

import com.example.centerofcat.data.api.services.ApiService
import com.example.centerofcat.data.repositories.CatRepositoryImpl
import com.example.centerofcat.domain.repositories.CatRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiFactoryModule::class, ApiServiceModule::class])
interface AppComponent {
    fun provideApi(): ApiService
    fun injectCatRepository(catRepository: CatRepositoryImpl)
}