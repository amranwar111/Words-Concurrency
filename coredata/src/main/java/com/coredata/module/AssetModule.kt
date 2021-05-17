package com.coredata.module

import android.content.Context
import com.coredomain.diinterfaces.AppContext
import com.coredomain.diinterfaces.ProjectScope
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ApplicationComponent
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetModule @Inject constructor( @AppContext val appContext: Context){

    fun getData(data:String) :String{
        return appContext.assets.open("mockdata${File.separator}$data.json").bufferedReader().use {
            it.readText()
        }
    }
}
