package com.example.interiordeisgnstudio1.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interiordeisgnstudio1.R
import com.example.interiordeisgnstudio1.UserRepoImpl
import com.example.interiordeisgnstudio1.viewmodel.UserViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginBody()
        }
    }
}

// ── BRAND COLORS ──
val LoginBrown = Color(0xFF5C3D2E)
val LoginWarmWhite = Color(0xFFFAF8F5)

@Composable
fun LoginBody() {
    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginWarmWhite)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "INTERIOR DESIGN STUDIO",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LoginBrown
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.height(150.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("email"),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LoginBrown,
                focusedLabelColor = LoginBrown,
                cursorColor = LoginBrown
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { visibility = !visibility }) {
                    Icon(
                        imageVector = if (visibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = LoginBrown
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LoginBrown,
                focusedLabelColor = LoginBrown,
                cursorColor = LoginBrown
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Forgot Password?",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // context.startActivity(Intent(context, ForgotPasswordActivity::class.java))
                },
            textAlign = TextAlign.End,
            color = LoginBrown,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(30.dp))

        // ... previous code (Spacer, Text, etc.)

        Button(
            onClick = {
                // 1. First, check if fields are empty
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Fields can't be empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // 2. Start the loading state
                loading = true

                // 3. PASTE YOUR CODE HERE:
                userViewModel.login(email.trim(), password.trim()) { success, msg ->
                    loading = false

                    // This will help you see why it's failing in the Logcat tab
                    android.util.Log.d("LOGIN_DEBUG", "Success: $success, Message: $msg")

                    if (success) {
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, ClientDashboardActivity::class.java))
                        (context as? ComponentActivity)?.finish()
                    } else {
                        // This will show you the exact Firebase error (e.g., "Invalid password")
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("login"),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LoginBrown)
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Log In", color = Color.White, fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(text = "Don’t have an account? ", color = Color.Gray)
            Text(
                text = "Create one",
                color = LoginBrown,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, RegistrationActivity::class.java))
                }
            )
        }
    }
}
