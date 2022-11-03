package com.osh.sample.main.impl.domain.impl.di

import com.osh.sample.main.impl.domain.MainScreenInteractor
import com.osh.sample.main.impl.domain.impl.MainScreenInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface MainFeatureDomainModule {
    @Binds
    fun bindMainScreenInteractor(impl: MainScreenInteractorImpl): MainScreenInteractor
}