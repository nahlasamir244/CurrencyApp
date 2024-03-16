package com.nahlasamir244.data.network.di

import com.nahlasamir244.data.network.repo.currency.CurrencyRemoteDataSource
import com.nahlasamir244.data.network.repo.currency.CurrencyRemoteDataSourceImpl
import com.nahlasamir244.data.network.repo.currency.CurrencyRepo
import com.nahlasamir244.data.network.repo.currency.CurrencyRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CurrencyRemoteDataSourceModule {

    @Binds
    abstract fun bindCurrencyRemoteDataSource(
        currencyRemoteDataSource: CurrencyRemoteDataSourceImpl
    ): CurrencyRemoteDataSource
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class CurrencyRepositoryModule {
    @Binds
    abstract fun bindCurrencyRepository(
        currencyRepo: CurrencyRepoImpl
    ): CurrencyRepo
}