package com.example.points.ui.plot

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.androidplot.xy.CatmullRomInterpolator
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.PanZoom
import com.androidplot.xy.SimpleXYSeries
import com.example.points.R
import com.example.points.databinding.FragmentPlotBinding
import kotlin.properties.Delegates

class PlotFragment : Fragment(R.layout.fragment_plot) {

    private val args by navArgs<PlotFragmentArgs>()
    private val binding by viewBinding(FragmentPlotBinding::bind)

    private val formatter by lazy { LineAndPointFormatter(Color.RED, Color.YELLOW, null, null) }

    private var panZoom: PanZoom by Delegates.notNull()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = PointListAdapter()

        binding.recyclerViewPoints.layoutManager = LinearLayoutManager(context)

        binding.recyclerViewPoints.adapter = adapter

        adapter.list = args.points.toList()

        val xVals = args.points.map { it.x }
        val yVals = args.points.map { it.y }

        val xySeries = SimpleXYSeries(xVals, yVals, "Points")

        binding.plot.setUserDomainOrigin(0);
        binding.plot.setUserRangeOrigin(0);

        panZoom = PanZoom.attach(
            binding.plot,
            PanZoom.Pan.BOTH,
            PanZoom.Zoom.STRETCH_BOTH,
            PanZoom.ZoomLimit.OUTER
        )

        binding.plot.addSeries(xySeries, formatter)

        binding.checkboxLines.setOnCheckedChangeListener { button, value ->
            redraw()
        }
        redraw()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("checkBox", binding.checkboxLines.isChecked)
        try {
            val state = panZoom.state
            state.apply(binding.plot)
            binding.plot.calculateMinMaxVals() // throws exception if state is empty
            outState.putSerializable("zoomState", state) // save only non empty state
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val state = savedInstanceState ?: return
        binding.checkboxLines.isChecked = state.getBoolean("checkBox", false)
        val zoomState = savedInstanceState.getSerializable("zoomState") as? PanZoom.State?
        if (zoomState != null) panZoom.state = zoomState
    }

    private fun redraw() {
        formatter.interpolationParams =
            if (binding.checkboxLines.isChecked)
                CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)
            else null
        binding.plot.redraw()
    }

}