package com.example.s8089387assignment2.ui.dashboard

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.s8089387assignment2.MainActivity
import com.example.s8089387assignment2.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    // requests DashboardViewModel from Hilt, scoped to this Activity's lifecycle
    private val viewModel: DashboardViewModel by viewModels()

    private lateinit var adapter: DashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val keypass = intent.getStringExtra(MainActivity.EXTRA_KEYPASS)
        if (keypass.isNullOrBlank()) {
            // shouldn't happen in normal use, but guards against a bad launch
            Toast.makeText(this, "Missing keypass, returning to login", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()
        observeUiState()
        viewModel.loadDashboard(keypass)
    }

    private fun setupRecyclerView() {
        val recyclerDashboard = findViewById<RecyclerView>(R.id.recyclerDashboard)
        adapter = DashboardAdapter { entity ->
            // navigation to Details screen wired up in a later commit
        }
        recyclerDashboard.adapter = adapter
    }

    // collects the ViewModel's state and updates the UI, only while the screen is visible
    private fun observeUiState() {
        val progressDashboard = findViewById<ProgressBar>(R.id.progressDashboard)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is DashboardUiState.Loading -> {
                            progressDashboard.visibility = android.view.View.VISIBLE
                        }
                        is DashboardUiState.Success -> {
                            progressDashboard.visibility = android.view.View.GONE
                            adapter.updateData(state.entities)
                        }
                        is DashboardUiState.Error -> {
                            progressDashboard.visibility = android.view.View.GONE
                            Toast.makeText(
                                this@DashboardActivity,
                                state.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}