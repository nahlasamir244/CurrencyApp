package com.nahlasamir244.data.network.di

import com.nahlasamir244.data.network.api.FixerApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Provides
    fun provideFixerApi(retrofit: Retrofit): FixerApiService =
        retrofit.create(FixerApiService::class.java)
}