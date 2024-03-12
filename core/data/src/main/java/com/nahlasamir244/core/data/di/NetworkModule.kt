package com.nahlasamir244.core.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.nahlasamir244.core.data.network.NetworkInterceptor
import com.nahlasamir244.core.data.utils.NetworkConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideChuckerInterceptor(@ApplicationContext mContext: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(mContext).build()
    }

    @Provides
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideOkHttpClient(
        networkInterceptor: NetworkInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(NetworkConstants.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(NetworkConstants.TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkConstants.TIME_OUT, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(5, 30, TimeUnit.SECONDS))
            .addInterceptor(networkInterceptor)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(httpLoggingInterceptor)
        return clientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.Base_Url)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory).build()
    }

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}