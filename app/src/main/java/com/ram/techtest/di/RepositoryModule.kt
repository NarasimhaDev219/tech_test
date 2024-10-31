package com.ram.techtest.di

import com.ram.techtest.news.data.repository.SourceDetailsRepository
import com.ram.techtest.news.data.repository.SourceDetailsRepositoryImpl
import com.ram.techtest.news.data.repository.SourceRepository
import com.ram.techtest.news.data.repository.SourceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesSourceRepository(
        sourceRepositoryImpl: SourceRepositoryImpl
    ):SourceRepository

    @Binds
    abstract fun providesSourceDetailsRepository(
        sourceDetailsRepositoryImpl: SourceDetailsRepositoryImpl
    ):SourceDetailsRepository
}