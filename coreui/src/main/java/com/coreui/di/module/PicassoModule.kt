package com.coreui.di.module

import android.content.Context
import com.coredomain.diinterfaces.AppContext
import com.jakewharton.picasso.OkHttp3Downloader
import okhttp3.OkHttpClient
import dagger.Provides
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
class PicassoModule {

    @Provides
    fun picasso(@AppContext context: Context, okHttp3Downloader: OkHttp3Downloader): Picasso {
        return Picasso.Builder(context).downloader(okHttp3Downloader).build()
    }

    @Provides
    fun okHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }




}