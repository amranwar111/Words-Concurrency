package com.coredata.module

import android.content.Context
import android.content.SharedPreferences
import com.coredomain.diinterfaces.AppContext
import com.coredomain.diinterfaces.AppPreferenceName
import com.coredomain.diinterfaces.ProjectScope
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ApplicationComponent


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class PreferenceModule @Inject
constructor(@AppContext context: Context, @AppPreferenceName prefName: String) {
    private val preferences: SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    fun registerOnSharedPreferenceChangeListener(): SharedPreferences = preferences
    var language: String?
        get() = this.getString("language", "en")
        set(currentLanguage) = putString("language", currentLanguage!!)


    private fun putString(key: String, value: String?) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun putInt(key: String, value: Int?) {
        val editor = preferences.edit()
        editor.putInt(key, value ?: 0)
        editor.apply()
    }

    private fun putBoolean(key: String, value: Boolean?) {
        val editor = preferences.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }


    private fun getString(key: String, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }

    private fun getInt(key: String, defaultValue: Int?): Int? {
        return preferences.getInt(key, defaultValue ?: 0)
    }

    private fun getBoolean(key: String, defaultValue: Boolean?): Boolean? {
        return preferences.getBoolean(key, defaultValue!!)
    }


    fun saveToken(token: String?) {
        putString("token", token)
    }

    fun savePrice(price: String?) {
        putString("Price", price)
    }

    fun getPrice(): String? {
        return getString("Price", null)
    }

    fun getToken(): String? {
        return getString("token", null)
    }

    fun removeToken() {
        putString("userData", null)
        putString("token", null)
    }

    fun isUser(): Boolean = getBoolean("customer", false)!!
    fun getUserType(): String = if (isUser()) "user" else "consultant"

    fun setAsUser() {
        putBoolean("customer", true)
    }

    fun savePlayerId(playerId: String?) {
        putString("PlayerId", playerId)
    }

    fun saveShareUrl(url: String?) {
        putString("ShareUrl", url)
    }

    fun getSharedUrl(): String? {
        return getString("ShareUrl", null)
    }

    fun getPlayerId(): String? {
        return getString("PlayerId", null)
    }

    fun saveUser(user: String) {
        putString("userData", user)
    }


    fun getUser(): String? {
        return getString("userData", null)
    }

    fun saveSettings(data: String?) {
        putString("settings", data)
    }

    fun getSettings(): String? {
        return getString("settings", null)
    }

    fun saveLoading(data: String?) {
        putString("Loading", data)
    }

    fun getLoading(): String? {
        return getString("Loading", "0")
    }

    fun saveUnseen(notifications: Int?) {
        putInt("unseen", notifications)
    }

    fun getUnseen(): Int? {
        return getInt("unseen", 0) ?: 0
    }

}
