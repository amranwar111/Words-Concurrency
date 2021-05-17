package com.coreui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.armdroid.rxfilechooser.chooser.RxFileChooser
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.LogUtils
import com.coredata.module.LanguageModule


import com.facebook.drawee.backends.pipeline.Fresco
import com.tripl3dev.prettystates.StatesConfigFactory
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import javax.inject.Inject

open class CoreApp : Application() {

    @Inject
    lateinit var language: LanguageModule
    var inBackground = false




    override fun onCreate() {
        super.onCreate()
        setCalligraphyFonts()
        RxFileChooser.register(this)
        initLog()
        initCrash()
        Fresco.initialize(this)



        StatesConfigFactory.intialize()
            .initDefaultViews()
        StatesConfigFactory.get()
            .setDefaultErrorLayout(R.layout.error_state_layout)
            .setDefaultLoadingLayout(R.layout.loading_state_layout)
            .setDefaultEmptyLayout(R.layout.empty_state_layout)

//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

    }


    fun getClligraphyFontPath(): String = "font/fontr.ttf"
    private fun setCalligraphyFonts() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(getClligraphyFontPath())
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }


    private fun initLog() {
        val config = LogUtils.getConfig()
            .setLogSwitch(BuildConfig.DEBUG)
            .setConsoleSwitch(BuildConfig.DEBUG)
            .setGlobalTag(null)
            .setLogHeadSwitch(true)
            .setLog2FileSwitch(false)
            .setDir("")
            .setFilePrefix("")
            .setBorderSwitch(true)
            .setSingleTagSwitch(true)
            .setConsoleFilter(LogUtils.V)
            .setFileFilter(LogUtils.V)
            .setStackDeep(1)
            .setStackOffset(0)
            .setSaveDays(3)
            .addFormatter(object : LogUtils.IFormatter<ArrayList<*>>() {
                override fun format(list: ArrayList<*>): String {
                    return "LogUtils Formatter ArrayList { " + list.toString() + " }"
                }
            })
        LogUtils.d(config.toString())
    }

    @SuppressLint("MissingPermission")
    private fun initCrash() {
        CrashUtils.init { crashInfo, _ ->
            LogUtils.e(crashInfo)
            AppUtils.relaunchApp()
        }
    }

    //MultiDex enable
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}