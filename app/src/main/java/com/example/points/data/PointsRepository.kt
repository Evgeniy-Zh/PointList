package com.example.points.data

import com.example.points.model.Point
import com.example.points.model.Result

interface PointsRepository {
    suspend fun getPoints(count: Int): Result<List<Point>>
}
