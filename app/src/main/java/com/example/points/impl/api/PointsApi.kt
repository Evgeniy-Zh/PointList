package com.example.points.impl.api

import androidx.annotation.Keep
import com.example.points.model.Point
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface PointsApi {

    @Keep
    class PointsResponse(@SerializedName("points") val points: List<Point>)

    @GET("/api/test/points")
    suspend fun getPoints(@Query("count") count: Int): PointsResponse
}