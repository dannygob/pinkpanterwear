package com.example.pinkpanterwear.data.di

import com.example.pinkpanterwear.data.network.FakeStoreApiService
import com.example.pinkpanterwear.data.network.RetrofitInstance
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Or appropriate component like ViewModelComponent if scoped differently
object NetworkModule {

    @Provides
    @Singleton // Assuming FakeStoreApiService should be a singleton
    fun provideFakeStoreApiService(): FakeStoreApiService {
        return RetrofitInstance.api
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
