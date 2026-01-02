package com.example.pinkpanterwear.di

import com.example.pinkpanterwear.AuthHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthHelper(): AuthHelper {
        return AuthHelper()
    }
}
