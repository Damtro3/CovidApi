package com.example.covidapi.Interfaces

import com.example.covidapi.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface IStorageService {
    fun injector(activity: MainActivity)
}