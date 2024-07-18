package com.example.points.impl.di

import com.example.points.data.PointsRepository
import com.example.points.impl.PointsRepositoryImpl
import com.example.points.impl.PointsRepositoryMockImpl
import com.example.points.impl.api.PointsApi
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .build()
}

fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .client(getHttpClient())
        .baseUrl("https://hr-challenge.dev.tapyou.com")
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()
}

fun getPointsRepository(): PointsRepository {
    return PointsRepositoryImpl(getRetrofit().create(PointsApi::class.java))
//    return PointsRepositoryMockImpl()
}