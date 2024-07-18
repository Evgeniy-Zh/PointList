package com.example.points.ui.plot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.points.R
import com.example.points.databinding.ItemPointBinding
import com.example.points.model.Point

class PointListAdapter : RecyclerView.Adapter<PointListAdapter.PointViewHolder>() {

    var list: List<Point> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PointViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding by viewBinding(ItemPointBinding::bind)
        fun bind(point: Point) {
            binding.text.text = "x: ${point.x} y: ${point.y}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val inflater :LayoutInflater = LayoutInflater.from(parent.context)
        return PointViewHolder(inflater.inflate(R.layout.item_point, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(list[position])
    }

}