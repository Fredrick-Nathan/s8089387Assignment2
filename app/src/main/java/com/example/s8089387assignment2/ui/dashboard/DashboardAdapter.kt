package com.example.s8089387assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s8089387assignment2.R
import com.example.s8089387assignment2.data.model.AnimalEntity

// holds the view reference for one row in the RecyclerView
class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvEntityInfo: TextView = itemView.findViewById(R.id.tvEntityInfo)

    // fills the row with the entity's fields, excluding description, and sets up the click listener
    fun bind(entity: AnimalEntity, onItemClick: (AnimalEntity) -> Unit) {
        tvEntityInfo.text = """
            Species: ${entity.species}
            Scientific name: ${entity.scientificName}
            Habitat: ${entity.habitat}
            Diet: ${entity.diet}
            Conservation status: ${entity.conservationStatus}
            Average lifespan: ${entity.averageLifespan} years
        """.trimIndent()

        itemView.setOnClickListener { onItemClick(entity) }
    }
}

// bridges the RecyclerView and the list of entities
class DashboardAdapter(
    private val onItemClick: (AnimalEntity) -> Unit
) : RecyclerView.Adapter<DashboardViewHolder>() {

    private var entities: List<AnimalEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(entities[position], onItemClick)
    }

    override fun getItemCount() = entities.size

    fun updateData(newEntities: List<AnimalEntity>) {
        entities = newEntities
        notifyDataSetChanged()
    }
}