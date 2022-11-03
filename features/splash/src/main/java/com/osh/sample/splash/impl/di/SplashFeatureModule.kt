package com.osh.sample.splash.impl.di

import com.osh.sample.splash.SplashFeatureApi
import com.osh.sample.splash.SplashFeatureNavigationGraphCreator
import com.osh.sample.splash.impl.SplashFeatureApiImpl
import com.osh.sample.splash.impl.navigation.SplashFeatureNavigationGraphCreatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class)
internal interface SplashFeatureModule {
    @Binds
    fun bindSplashFeatureApi(impl: SplashFeatureApiImpl): SplashFeatureApi
}

@Module
@InstallIn(SingletonComponent::class)
internal interface SplashFeatureNavigationGraphCreatorModule {
    @Binds
    fun bindSplashFeatureNavigationGraphCreator(impl: SplashFeatureNavigationGraphCreatorImpl): SplashFeatureNavigationGraphCreator
}