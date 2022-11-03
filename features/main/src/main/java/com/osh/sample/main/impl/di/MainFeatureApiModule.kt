package com.osh.sample.main.impl.di

import com.osh.sample.main.MainFeatureApi
import com.osh.sample.main.impl.MainFeatureApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
internal interface MainFeatureApiModule {
    @Binds
    fun bindMainFeatureApi(impl: MainFeatureApiImpl): MainFeatureApi
}