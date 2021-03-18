package com.example.mypokedex.di

import com.example.mypokedex.network.PokedexClient
import com.example.mypokedex.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideHomeRepository(
        pokedexClient: PokedexClient
    ): HomeRepository {
        return HomeRepository(pokedexClient)
    }
}