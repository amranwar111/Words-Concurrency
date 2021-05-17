package com.coreui.di.module

import android.content.Context
import com.blankj.utilcode.util.NetworkUtils
import com.coredata.module.PreferenceModule
import com.coredomain.diinterfaces.AppContext
import dagger.Module
import okhttp3.logging.HttpLoggingInterceptor
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import java.io.File
import java.util.concurrent.TimeUnit

import okhttp3.*
import timber.log.Timber

@InstallIn(ApplicationComponent::class)
@Module(includes = [ApplicationModule::class])
class OkHttpClientModule {

    @Provides
    fun cache(@AppContext context: Context): Cache {
        val cacheSize = (5 * 1024 * 1024).toLong()
        return Cache(context.cacheDir, cacheSize)
    }




    @Provides
    fun okHttpClient(@AppContext context: Context,responseInterceptor:ResponseInterceptor, httpLoggingInterceptor: HttpLoggingInterceptor, preference: PreferenceModule
    ): OkHttpClient {
        val httpCacheDirectory = File(context.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        return OkHttpClient()
                .newBuilder()
                .cache(cache)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
//                .addNetworkInterceptor (REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor{ chain ->
                    val original = chain?.request()
                    val request = original?.newBuilder()
                    if (preference.getToken() != null) {
                        request?.header("Authorization", "bearer ${preference.getToken()!!}")
                    }

                    request?.header("Content-Type", "application/json")
                            ?.method(original?.method, original?.body)
//                    if (!NetworkUtils.isConnected()) {
//                        val cacheControl = CacheControl.Builder()
//                                .maxStale(7, TimeUnit.DAYS)
//                                .build()
//
//                        request.cacheControl(cacheControl)
//
//                    }
                    chain?.proceed(request?.build()) }
                .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(responseInterceptor)
//                .addNetworkInterceptor { chain ->
//                    val response = chain.proceed(chain.request())
//
//                    if (NetworkUtils.isConnected()) {
//                        // re-write response header to force use of cache
//                        val cacheControl = CacheControl.Builder()
//                                .maxAge(1, TimeUnit.SECONDS)
//                                .build()
//
//
//                        response.newBuilder()
//                                .header("Cache-Control", cacheControl.toString())
//                                .build()
//                    }else{
//                        // re-write response header to force use of cache
//                        val cacheControl = CacheControl.Builder()
//                                .maxAge(2, TimeUnit.MINUTES)
//                                .build()
//
//
//                        response.newBuilder()
//                                .header("Cache-Control", cacheControl.toString())
//                                .build()
//                    }
//                }
                .build()
    }







    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }




}