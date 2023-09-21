package me.aleksandarzekovic.labsearchwithpaging3

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TmdbApplication : Application() {

    companion object {
        var instance: TmdbApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
