package com.example.interiordeisgnstudio1.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interiordeisgnstudio1.R
import com.example.interiordeisgnstudio1.ui.theme.AccentBrown
import com.example.interiordeisgnstudio1.ui.theme.Cream
import com.example.interiordeisgnstudio1.ui.theme.EspressoBrown
import com.example.interiordeisgnstudio1.ui.theme.Mocha
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            InteriorSplash()
        }
    }
}

@Composable
fun InteriorSplash() {

    val context = LocalContext.current
    val activity = context as Activity

    // Splash delay + navigation
    LaunchedEffect(Unit) {
        delay(2500)
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
        activity.finish()
    }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Cream),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // App Logo
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Interior Logo",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "INTERIOR DESIGN",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = EspressoBrown,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Infusing life into spaces with elegance",
                fontSize = 14.sp,
                color = Mocha.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(50.dp))

            CircularProgressIndicator(
                color = AccentBrown,
                strokeWidth = 3.dp
            )
        }
    }
}