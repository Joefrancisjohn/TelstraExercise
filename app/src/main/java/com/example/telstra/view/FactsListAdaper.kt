package com.example.telstra.view


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.telstra.R
import com.example.telstra.databinding.FactsItemBinding
import com.example.telstra.model.Row
import java.util.*

class FactsListAdaper : RecyclerView.Adapter<FactsListAdaper.FactsListViewHolder>() {

    private var data: List<Row> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactsListViewHolder {
        val binding: FactsItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.facts_item,
                parent,
                false)
        return FactsListViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FactsListViewHolder, position: Int) =
        holder.bind(data[position])

    fun swapData(data: List<Row>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FactsListViewHolder(val binding: FactsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Row) = with(itemView) {
            binding.factsRow = item
            binding.executePendingBindings()
            setOnClickListener {
                // TODO: Handle on click
            }
        }
    }
}