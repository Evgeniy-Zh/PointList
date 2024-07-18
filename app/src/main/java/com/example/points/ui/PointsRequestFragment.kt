package com.example.points.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.points.ui.PointsViewModel.*
import com.example.points.R
import com.example.points.databinding.FragmentPointsRequestBinding
import com.example.points.model.Point


class PointsRequestFragment : Fragment(R.layout.fragment_points_request) {

    private val viewModel: PointsViewModel by viewModels()

    private val bindings by viewBinding(FragmentPointsRequestBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            setProgress(false)
            showError(null)
            when (state) {
                is State.Error -> showError(state.error)
                is State.Success -> showPlot(state.points)
                State.Progress -> setProgress(true)
                State.Idle -> {}
            }
        }

        bindings.buttonStart.setOnClickListener {
            val countStr = bindings.editTextCount.text.toString()
            viewModel.processAction(Action.LoadData(countStr))
        }

    }

    private fun showPlot(points: List<Point>) {
        findNavController().navigate(
            PointsRequestFragmentDirections.actionPlotRequestFragmentToPlotFragment(
                points.toTypedArray()
            )
        )
        viewModel.processAction(Action.DataIsShown)
    }

    private fun showError(error: Throwable?) {
        if (error != null)
            bindings.errorMessage.text = error.message
        else
            bindings.errorMessage.text = ""
    }

    private fun setProgress(value: Boolean) {
        bindings.progress.isInvisible = !value
        bindings.buttonStart.isEnabled = !value
    }

}