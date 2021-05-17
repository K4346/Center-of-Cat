package com.example.centerofcat.app.di

import com.example.centerofcat.app.ui.adapters.CatListAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AdapterModule {
    @Provides
    @Singleton
    fun provideAdapter(): CatListAdapter {
        val catDiffUtilCallback = com.example.centerofcat.app.ui.adapters.CatDiffUtilCallback()
        return CatListAdapter(catDiffUtilCallback)
    }
}