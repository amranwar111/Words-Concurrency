package com.coreui.di.module

import android.app.Application
import android.content.Context
import com.coredomain.diinterfaces.AppContext
import com.coredomain.diinterfaces.AppPreferenceName
import com.coredomain.diinterfaces.AppRemoteUrl
import com.coreui.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent

val DOMAIN = BuildConfig.DOMAIN
@InstallIn(ApplicationComponent::class)
@Module
class ApplicationModule {

    @AppRemoteUrl
    @Provides
    fun serviceURl(): String {
        return "$DOMAIN/api/v1/"
    }


    @AppPreferenceName
    @Provides
    fun setPreferenceName(): String {
        return BuildConfig.PreferenceName
    }


    @AppContext
    @Provides
    fun context(application: Application): Context {
        return application.applicationContext
    }

}