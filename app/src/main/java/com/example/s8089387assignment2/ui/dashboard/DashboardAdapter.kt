package com.example.s8089387assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s8089387assignment2.R
import com.google.gson.JsonObject

// holds the view reference for one row in the RecyclerView
class DashboardViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
    private val tvEntityInfo: TextView = itemView.findViewById(R.id.tvEntityInfo)

    // fills the row with the entity's fields, excluding description, and sets up the click listener
    fun bind(entity: JsonObject, onItemClick: (JsonObject) -> Unit) {
        // build a readable "key: value" line for every field except description
        val displayText = entity.entrySet()
            .filter { it.key != "description" }
            .joinToString("\n") { (key, value) -> "$key: ${value.asString}" }

        tvEntityInfo.text = displayText
        itemView.setOnClickListener { onItemClick(entity) }
    }
}

// bridges the RecyclerView and the list of entities
class DashboardAdapter(
    private val onItemClick: (JsonObject) -> Unit
) : RecyclerView.Adapter<DashboardViewHolder>() {

    private var entities: List<JsonObject> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(entities[position], onItemClick)
    }

    override fun getItemCount() = entities.size

    // called by the Activity when new data arrives from the ViewModel
    fun updateData(newEntities: List<JsonObject>) {
        entities = newEntities
        notifyDataSetChanged()
    }
}