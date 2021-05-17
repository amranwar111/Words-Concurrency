package com.trippl3dev.listlibrary

import android.content.Context

class PrettyListConfiguration(val context: Context?){

    companion object {
        fun getInstance(context: Context?): PrettyListConfiguration {
            return PrettyListConfiguration(context = context)
        }
    }
    private var preferenceModule: PreferenceModule? = null
    fun setLoadingViewId(id:Int):PrettyListConfiguration{
        if (preferenceModule == null){
            preferenceModule = PreferenceModule.getInstance(context = context!!)
        }
        preferenceModule?.loadingId = id
        return this
    }

    fun setErrorView(id:Int):PrettyListConfiguration{
        if (preferenceModule == null){
            preferenceModule = PreferenceModule.getInstance(context = context!!)
        }
        preferenceModule?.loadingId = id
        return this
    }
}