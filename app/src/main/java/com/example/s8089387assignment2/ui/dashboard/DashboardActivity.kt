package com.example.s8089387assignment2.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.s8089387assignment2.R
import dagger.hilt.android.AndroidEntryPoint

// bare shell for now; RecyclerView adapter and ViewModel wiring come in the next commit
@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }
}