package com.example.points.impl.api

import com.example.points.model.Point
import retrofit2.http.GET
import retrofit2.http.Query

interface PointsApi {
    class PointsResponse(val points: List<Point>)

    @GET("/api/test/points")
    suspend fun getPoints(@Query("count") count: Int): PointsResponse
}