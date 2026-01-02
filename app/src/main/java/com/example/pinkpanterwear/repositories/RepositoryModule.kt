package com.example.pinkpanterwear.repositories

import com.example.pinkpanterwear.repository.CategoryRepositoryImpl
import com.example.pinkpanterwear.repository.FirebaseCartRepositoryImpl
import com.example.pinkpanterwear.repository.FirestoreProductRepositoryImpl
import com.example.pinkpanterwear.repository.OrderRepositoryImpl
import com.example.pinkpanterwear.repository.WishlistRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindProductRepository(
        firestoreProductRepositoryImpl: FirestoreProductRepositoryImpl,
    ): ProductRepository

    @Binds
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Singleton
    @Binds
    abstract fun bindCartRepository(
        firebaseCartRepositoryImpl: FirebaseCartRepositoryImpl,
    ): CartRepository

    @Singleton
    @Binds
    abstract fun bindWishlistRepository(
        wishlistRepositoryImpl: WishlistRepositoryImpl
    ): WishlistRepository
}