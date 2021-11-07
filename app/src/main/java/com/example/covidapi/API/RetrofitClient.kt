package com.example.covidapi.API

import android.content.Context
import com.example.covidapi.Services.StorageService
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RetrofitClient @Inject constructor() {
    @Inject
    lateinit var storage:StorageService
    //val component = Dagger.create()

    var urlLast: String? = null
    var urlGetFromStorage: String? = null
    var client: OkHttpClient? = null
    fun GenerateClient(Url: String, context: Context): Retrofit? {
       // component.inkector2()
        storage = StorageService()
        client = OkHttpClient().newBuilder()
            .connectTimeout(36000, TimeUnit.SECONDS)
            .writeTimeout(36000, TimeUnit.SECONDS)
            .readTimeout(36000, TimeUnit.SECONDS)
            .build()
        urlLast = storage
            .getPropertyString(context, "lastKnownApiAddress", "http://192.168.1.6/dashapi/api/")
        urlGetFromStorage = storage
            .getPropertyString(context, "apiAddress", "http://192.168.1.6/dashapi/api/")
        val gson: Gson = Gson().newBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
        return if (retrofit == null) {
            storage
                .setPropertyString(context, "lastKnownApiAddress", urlGetFromStorage)
            retrofit = Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
            retrofit
        } else {
            if (urlLast == urlGetFromStorage) retrofit else {
                storage
                    .setPropertyString(context, "lastKnownApiAddress", urlGetFromStorage)
                retrofit = Retrofit.Builder()
                    .baseUrl(Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()
                retrofit
            }
        }
    }

    companion object {
        var retrofit: Retrofit? = null
    }
}