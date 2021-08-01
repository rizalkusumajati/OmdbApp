package com.example.omdbapp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FakeBaseUrlModule {
    @Singleton
    @Provides
    fun provideBaseUrl(): String = "http://localhost:8080/"
}
