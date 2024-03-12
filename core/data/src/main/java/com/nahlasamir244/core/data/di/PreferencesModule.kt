package com.nahlasamir244.core.data.di

import com.nahlasamir244.core.data.sharedpref.ISharedPreferencesRepo
import com.nahlasamir244.core.data.sharedpref.SharedPreferencesRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
abstract class PreferencesModule {

    @Binds
    abstract fun bindSharedPreferencesRepo(preferencesRepo: SharedPreferencesRepo): ISharedPreferencesRepo

}