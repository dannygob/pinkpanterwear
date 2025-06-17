package com.example.pinkpanterwear.data.di

import android.content.Context
import androidx.room.Room
import com.example.pinkpanterwear.data.local.database.AppDatabase
import com.example.pinkpanterwear.data.local.database.dao.CartDao
import com.example.pinkpanterwear.data.local.database.dao.WishlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "pinkpanterwear_database" // Name for the database file
        ).build()
        // Add .fallbackToDestructiveMigration() if you want to handle migrations easily during development
    }

    @Provides
    @Singleton // DAOs are typically singletons if the DB is a singleton
    fun provideCartDao(appDatabase: AppDatabase): CartDao {
        return appDatabase.cartDao()
    }

    @Provides
    @Singleton
    fun provideWishlistDao(appDatabase: AppDatabase): WishlistDao {
        return appDatabase.wishlistDao()
    }
}
