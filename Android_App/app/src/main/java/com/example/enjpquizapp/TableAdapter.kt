package com.example.enjpquizapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TableAdapter(private val items: List<TableItem>) :
    RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.japTextView.text = item.japColumn
        holder.engTextView.text = item.engColumn
        holder.romaTextView.text = item.romaColumn
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val japTextView: TextView = itemView.findViewById(R.id.japTextView)
        val engTextView: TextView = itemView.findViewById(R.id.engTextView)
        val romaTextView: TextView = itemView.findViewById(R.id.romaTextView)
    }
}