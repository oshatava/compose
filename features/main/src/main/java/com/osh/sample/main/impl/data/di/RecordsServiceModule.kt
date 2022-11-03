package com.osh.sample.main.impl.data.di

import com.osh.sample.main.MainFeatureConfig
import com.osh.sample.main.impl.data.mapper.RecordItemsMapper
import com.osh.sample.main.impl.data.mapper.RecordItemsMapperImpl
import com.osh.sample.main.impl.data.service.RecordsService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal interface RecordsServiceModule {

    @Binds
    fun provideRecordItemsMapper(impl: RecordItemsMapperImpl): RecordItemsMapper

    companion object {

        @Provides
        fun provideRetrofit(config: MainFeatureConfig): Retrofit {
            return Retrofit.Builder()
                .baseUrl(config.baseAPIUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun provideRecordsService(retrofit: Retrofit) = retrofit.create(RecordsService::class.java)

    }
}