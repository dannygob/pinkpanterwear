package com.example.pinkpanterwear.data.di

import com.example.pinkpanterwear.data.repository.CategoryRepositoryImpl
import com.example.pinkpanterwear.data.repository.ProductRepositoryImpl
import com.example.pinkpanterwear.data.repository.OrderRepositoryImpl
import com.example.pinkpanterwear.data.repository.CartRepositoryImpl
import com.example.pinkpanterwear.data.repository.WishlistRepositoryImpl
import com.example.pinkpanterwear.domain.repository.CategoryRepository
import com.example.pinkpanterwear.domain.repository.ProductRepository
import com.example.pinkpanterwear.domain.repository.OrderRepository
import com.example.pinkpanterwear.domain.repository.CartRepository
import com.example.pinkpanterwear.domain.repository.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent // Or appropriate component

@Module
@InstallIn(SingletonComponent::class) // Or appropriate component like ViewModelComponent
abstract class RepositoryModule {

    @Binds
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    @Binds
    abstract fun bindWishlistRepository(
        wishlistRepositoryImpl: WishlistRepositoryImpl
    ): WishlistRepository
}
