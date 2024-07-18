package com.example.points.model

sealed class Result<T> {
    class Success<T>(val value: T) : Result<T>()
    class Error<T>(val throwable: Throwable) : Result<T>()
}
