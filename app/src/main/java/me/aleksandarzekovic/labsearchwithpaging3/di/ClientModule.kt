package me.aleksandarzekovic.labsearchwithpaging3.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {

    @Provides
    @Singleton
    fun provideClient(
        @HostSelectorInterceptor hostSelectionInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(hostSelectionInterceptor)
        .build()
}
