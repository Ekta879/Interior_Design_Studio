package com.example.interiordeisgnstudio1.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interiordeisgnstudio1.R
import com.example.interiordeisgnstudio1.UserRepoImpl
import com.example.interiordeisgnstudio1.viewmodel.UserViewModel

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InteriorForgotPasswordScreen()
        }
    }
}

@Composable
fun InteriorForgotPasswordScreen() {

    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    var email by remember { mutableStateOf("") }

    val PrimaryBrown = Color(0xFF6D4C41)
    val LightBackground = Color(0xFFF5F5F5)
    val DarkText = Color(0xFF4E342E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        // Add interior_logo.png in drawable
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Reset Password",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBrown
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Enter your registered email to receive a password reset link.",
            fontSize = 14.sp,
            color = DarkText.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (email.isBlank()) {
                    Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                } else {
                    userViewModel.forgetPassword(email.trim()) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        if (success) {
                            context.startActivity(
                                Intent(context, LoginActivity::class.java)
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBrown)
        ) {
            Text("Send Reset Link", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(
            onClick = {
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        ) {
            Text("Back to Login", color = PrimaryBrown)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    InteriorForgotPasswordScreen()
}
