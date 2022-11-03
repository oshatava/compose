package com.osh.compose.di

import com.osh.sample.main.MainFeatureConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface FeaturesDependencyModule {

    companion object {
        @Provides
        fun providesMainFeatureConfig(): MainFeatureConfig {
            return MainFeatureConfig(
                baseAPIUrl = "https://www.mocky.io/v2/"
            )
        }
    }
}