package com.example.covidapi.Interfaces

import com.example.covidapi.API.RetrofitClient
import com.example.covidapi.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface IRetrofitClient {
    fun injector(activity: MainActivity)

}
