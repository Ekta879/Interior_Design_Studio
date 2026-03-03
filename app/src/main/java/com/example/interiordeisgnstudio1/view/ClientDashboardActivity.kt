package com.example.interiordeisgnstudio1.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ClientDashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientDashboard()
        }
    }
}