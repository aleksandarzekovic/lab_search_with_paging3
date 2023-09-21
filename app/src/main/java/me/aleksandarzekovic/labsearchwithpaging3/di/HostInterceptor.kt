package me.aleksandarzekovic.labsearchwithpaging3.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Qualifier
import javax.inject.Singleton
import me.aleksandarzekovic.labsearchwithpaging3.BuildConfig

const val AUTH_TOKEN = "Authorization"

@Module
@InstallIn(SingletonComponent::class)
object HostSelectionInterceptor {

    @Provides
    @Singleton
    @HostSelectorInterceptor
    fun provideHostSelectorInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .header(AUTH_TOKEN, BuildConfig.TMDB_API_KEY)
                .build()
            chain.proceed(newRequest)
        }
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class HostSelectorInterceptor
