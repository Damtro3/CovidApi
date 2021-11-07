package com.example.covidapi.Services

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageService @Inject constructor() {
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    companion object {
        const val SharedPreferencesVault = "mCovidPreferences"
    }
    fun setPropertyDate(context: Context, key: String?, value: Long?)
    {
        sharedPref = context.getSharedPreferences(SharedPreferencesVault, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        editor.putLong(key, value!!)
        editor.commit()
    }

    fun getPropertyDate(
        context: Context,
        key: String?,
        value: Long?
    ): Long {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        return sharedPref.getLong(key, value!!)
    }

    fun setPropertyInt(
        context: Context,
        key: String?,
        value: Int
    ) {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun getPropertyInt(context: Context, key: String?, value: Int): Int {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        return sharedPref.getInt(key, value)
    }

    fun setPropertyString(
        context: Context,
        key: String?,
        value: String?
    ) {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        editor = sharedPref.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getPropertyString(
        context: Context,
        key: String?,
        value: String?
    ): String? {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        return sharedPref.getString(key, value)
    }

    fun setPropertyBool(
        context: Context,
        key: String?,
        value: Boolean?
    ) {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        editor = sharedPref.edit()
        editor.putBoolean(key, value!!)
        editor.commit()
    }

    fun getPropertyBool(
        context: Context,
        key: String?,
        value: Boolean
    ): Boolean {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        return sharedPref.getBoolean(key, value)
    }

    fun setPropertyLong(
        context: Context,
        key: String?,
        value: Long?
    ) {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        editor = sharedPref.edit()
        editor.putLong(key, value!!)
        editor.commit()
    }

    fun getPropertyLong(
        context: Context,
        key: String?,
        value: Long
    ): Long {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        return sharedPref.getLong(key, value)
    }

    fun setPropertyStringList(
        context: Context,
        key: String?,
        list: List<String?>?
    ) {
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    fun getPropertyStringList(
        context: Context,
        key: String?,
        list: List<*>?
    ): List<*>? {
        var list = list
        sharedPref = context.getSharedPreferences(
            SharedPreferencesVault,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPref.getString(key, null)
        val type =
            object : TypeToken<List<String?>?>() {}.type
        list = gson.fromJson<List<*>>(json, type)
        return list
    }


}