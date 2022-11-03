package com.osh.sample.main.impl.data.di

import com.osh.sample.main.impl.data.RecordsRepositoryImpl
import com.osh.sample.main.impl.domain.RecordsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface MainFeatureDataModule {
    @Binds
    fun bindListScreenItemsRepository(impl: RecordsRepositoryImpl): RecordsRepository
}