package com.example.points.impl

import com.example.points.data.PointsRepository
import com.example.points.impl.api.PointsApi
import com.example.points.model.Point
import com.example.points.model.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.cos
import kotlin.random.Random
import kotlin.random.nextInt

class PointsRepositoryImpl(
    private val api: PointsApi
) : PointsRepository {

    override suspend fun getPoints(count: Int): Result<List<Point>> =
        withContext(Dispatchers.Default) {
            try {

                val points =
                    api.getPoints(count).points.sortedBy { it.x }

                return@withContext Result.Success(points)

            } catch (e: Exception) {
                if (e is CancellationException) throw e
                return@withContext Result.Error(e)
            }
        }


}

class PointsRepositoryMockImpl : PointsRepository {

    override suspend fun getPoints(count: Int): Result<List<Point>> =
        withContext(Dispatchers.Default) {
            delay(2000)

            if (count < 3)
                return@withContext Result.Error(Exception("Random exception"))

            val points = (-10 until count - 10).toList().map {
                val x = it / 5f
                Point(x, f(x))
            }
            return@withContext Result.Success(points)
        }

    private fun f(x: Float): Float {
        return cos(x) + (Random.nextInt(-1..1)) * 0.1f
    }

}