package com.aditya.movieapp.util

import com.aditya.movieapp.interfaces.MovieApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private var INSTANCE:Retrofit?=null
    private fun getRetrofit():Retrofit{
        if(INSTANCE==null){
            val client=OkHttpClient.Builder().build()
            INSTANCE=Retrofit.Builder().baseUrl(Constant.baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return INSTANCE!!
    }
    fun getApiService(): MovieApi {
        return getRetrofit().create(MovieApi::class.java)
    }
}