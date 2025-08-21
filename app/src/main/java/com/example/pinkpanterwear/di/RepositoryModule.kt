package com.example.pinkpanterwear.di


import com.example.pinkpanterwear.repositories.CartRepository
import com.example.pinkpanterwear.repositories.CategoryRepository
import com.example.pinkpanterwear.repositories.OrderRepository
import com.example.pinkpanterwear.repositories.WishlistRepository
import com.example.pinkpanterwear.repository.CartRepositoryImpl
import com.example.pinkpanterwear.repository.CategoryRepositoryImpl
import com.example.pinkpanterwear.repository.OrderRepositoryImpl
import com.example.pinkpanterwear.repository.ProductRepositoryImpl
import com.example.pinkpanterwear.repository.WishlistRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
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

    companion object {
        @Provides
        fun provideWishlistRepository(
            firestore: FirebaseFirestore,
        ): WishlistRepository {
            return WishlistRepositoryImpl(firestore)
        }
    }
}
