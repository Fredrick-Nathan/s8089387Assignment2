package com.example.s8089387assignment2.ui.details

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.s8089387assignment2.R

class DetailsActivity : AppCompatActivity() {

    companion object {
        // keys used to pass the selected animal's fields via Intent extras
        const val EXTRA_SPECIES = "extra_species"
        const val EXTRA_SCIENTIFIC_NAME = "extra_scientific_name"
        const val EXTRA_HABITAT = "extra_habitat"
        const val EXTRA_DIET = "extra_diet"
        const val EXTRA_CONSERVATION_STATUS = "extra_conservation_status"
        const val EXTRA_LIFESPAN = "extra_lifespan"
        const val EXTRA_DESCRIPTION = "extra_description"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val species = intent.getStringExtra(EXTRA_SPECIES) ?: ""
        val scientificName = intent.getStringExtra(EXTRA_SCIENTIFIC_NAME) ?: ""
        val habitat = intent.getStringExtra(EXTRA_HABITAT) ?: ""
        val diet = intent.getStringExtra(EXTRA_DIET) ?: ""
        val conservationStatus = intent.getStringExtra(EXTRA_CONSERVATION_STATUS) ?: ""
        val lifespan = intent.getIntExtra(EXTRA_LIFESPAN, 0)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: ""

        findViewById<TextView>(R.id.tvDetailsTitle).text = species
        findViewById<TextView>(R.id.tvScientificName).text = scientificName
        findViewById<TextView>(R.id.tvHabitat).text = "Habitat: $habitat"
        findViewById<TextView>(R.id.tvDiet).text = "Diet: $diet"
        findViewById<TextView>(R.id.tvConservationStatus).text = "Conservation status: $conservationStatus"
        findViewById<TextView>(R.id.tvLifespan).text = "Average lifespan: $lifespan years"
        findViewById<TextView>(R.id.tvDescription).text = description
    }
}