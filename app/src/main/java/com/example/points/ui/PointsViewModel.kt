package com.example.points.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.points.data.PointsRepository
import com.example.points.impl.di.getPointsRepository
import com.example.points.model.Point
import com.example.points.model.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class PointsViewModel(
    private val repository: PointsRepository = getPointsRepository() //TODO: ViewModelFactory
) : ViewModel() {

    sealed class Action {
        class LoadData(val countString: String) : Action()
        object DataIsShown : Action()
    }

    sealed class State {
        object Idle : State()
        object Progress : State()
        class Success(val points: List<Point>) : State()
        class Error(val error: Throwable) : State()
    }

    private val channel = Channel<Action>()

    val stateLiveData: LiveData<State> = liveData {
        while (true) {

            val action = channel.receiveCatching().getOrNull()

            when (action) {
                is Action.LoadData -> loadData(action.countString)
                Action.DataIsShown -> dataIsShown()
                null -> break
            }

        }
    }

    fun processAction(action: Action) = viewModelScope.launch {
        channel.send(action)
    }


    private suspend fun LiveDataScope<State>.loadData(countString: String) {
        val count = countString.toIntOrNull() ?: -1

        emit(State.Progress)

        val pointsResult =
            repository.getPoints(count)

        when (pointsResult) {
            is Result.Error -> emit(State.Error(pointsResult.throwable))
            is Result.Success -> emit(State.Success(pointsResult.value))
        }
    }

    private suspend fun LiveDataScope<State>.dataIsShown() {
        emit(State.Idle)
    }
}