package com.example.s8089387assignment2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.s8089387assignment2.ui.login.LoginUiState
import com.example.s8089387assignment2.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// required on any Activity that injects Hilt dependencies, like the ViewModel below
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // requests LoginViewModel from Hilt, scoped to this Activity's lifecycle
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupLoginButton()
        observeUiState()
    }

    // grabs the entered credentials and triggers the login attempt on click
    private fun setupLoginButton() {
        val etUsername = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            viewModel.login(username, password)
        }
    }

    // collects the ViewModel's state and updates the UI accordingly, only while the screen is visible
    private fun observeUiState() {
        val progressLogin = findViewById<android.widget.ProgressBar>(R.id.progressLogin)
        val tvLoginError = findViewById<android.widget.TextView>(R.id.tvLoginError)
        val btnLogin = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogin)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is LoginUiState.Idle -> {
                            progressLogin.visibility = android.view.View.GONE
                            tvLoginError.visibility = android.view.View.GONE
                            btnLogin.isEnabled = true
                        }
                        is LoginUiState.Loading -> {
                            progressLogin.visibility = android.view.View.VISIBLE
                            tvLoginError.visibility = android.view.View.GONE
                            btnLogin.isEnabled = false
                        }
                        is LoginUiState.Success -> {
                            progressLogin.visibility = android.view.View.GONE
                            btnLogin.isEnabled = true
                            // placeholder until Dashboard screen exists (commit #10 adds real navigation)
                            Toast.makeText(
                                this@MainActivity,
                                "Login successful. Keypass: ${state.keypass}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is LoginUiState.Error -> {
                            progressLogin.visibility = android.view.View.GONE
                            btnLogin.isEnabled = true
                            tvLoginError.text = state.message
                            tvLoginError.visibility = android.view.View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}