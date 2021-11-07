package com.example.covidapi.Interfaces

import com.example.covidapi.Models.Countries
import io.reactivex.Single
import retrofit2.Call

import retrofit2.http.GET
import java.util.*

interface IGets {

    @GET("v1/vaccines?continent=Europe")
    fun GET_Countries(): Single<Object>

    @GET("v1/cases")
    fun GET_Countriess(): Call<Object>
}