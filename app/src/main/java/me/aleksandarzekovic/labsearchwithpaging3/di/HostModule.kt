package me.aleksandarzekovic.labsearchwithpaging3.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Singleton

private const val HOST = "https://api.themoviedb.org/"

@Module
@InstallIn(SingletonComponent::class)
object HostModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): HttpUrl = HOST.toHttpUrl()
}