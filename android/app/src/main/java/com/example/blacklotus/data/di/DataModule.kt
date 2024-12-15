package com.example.blacklotus.data.di

import com.example.blacklotus.data.repository.DiscountRepository
import com.example.blacklotus.data.repository.DiscountRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsDiscountRepository(
        discountRepository: DiscountRepositoryImp
    ): DiscountRepository

}