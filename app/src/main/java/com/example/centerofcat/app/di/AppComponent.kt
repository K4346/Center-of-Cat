package com.example.centerofcat.app.di

import com.example.centerofcat.app.ui.catFavourites.FavouritesCatsFragment
import com.example.centerofcat.app.ui.catList.CatsListFragment
import com.example.centerofcat.app.ui.loadCat.LoadCatFragment
import com.example.centerofcat.data.api.services.ApiService
import com.example.centerofcat.data.repositories.CatRepositoryImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiFactoryModule::class, ApiServiceModule::class, AdapterModule::class])
interface AppComponent {
    fun provideApi(): ApiService
    fun injectCatRepository(catRepository: CatRepositoryImpl)
    fun injectAdapter(fragment: CatsListFragment)
    fun injectAdapter(fragment: FavouritesCatsFragment)
    fun injectAdapter(fragment: LoadCatFragment)
}