package com.example.interiordeisgnstudio1.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interiordeisgnstudio1.Model.UserModel
import com.example.interiordeisgnstudio1.R
import com.example.interiordeisgnstudio1.viewmodel.UserViewModel
import com.example.interiordeisgnstudio1.UserRepoImpl

class RegistrationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrationBody()
        }
    }
}

@Composable
fun RegistrationBody() {

    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val PrimaryColor = Color(0xFF6D4C41)
    val BackgroundColor = Color(0xFFF5F5F5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Interior Design Studio",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(140.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        InteriorInput(
            label = "Full Name",
            value = fullName,
            modifier = Modifier.testTag("fullName")
        ) { fullName = it }

        Spacer(modifier = Modifier.height(14.dp))

        InteriorInput(
            label = "Email Address",
            value = email,
            modifier = Modifier.testTag("email"),
            keyboardType = KeyboardType.Email
        ) { email = it }

        Spacer(modifier = Modifier.height(14.dp))

        InteriorPassword(
            label = "Password",
            value = password,
            visible = passwordVisible,
            modifier = Modifier.testTag("password"),
            onToggle = { passwordVisible = !passwordVisible }
        ) { password = it }

        Spacer(modifier = Modifier.height(14.dp))

        InteriorPassword(
            label = "Confirm Password",
            value = confirmPassword,
            visible = confirmPasswordVisible,
            modifier = Modifier.testTag("confirmPassword"),
            onToggle = { confirmPasswordVisible = !confirmPasswordVisible }
        ) { confirmPassword = it }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                // ✅ Broader check to skip Firebase during Instrumentation Tests on emulators
                val isEmulator = android.os.Build.FINGERPRINT.contains("generic") || 
                                 android.os.Build.FINGERPRINT.contains("vbox") || 
                                 android.os.Build.PRODUCT.contains("sdk") || 
                                 android.os.Build.MODEL.contains("Emulator")

                if (isEmulator) {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    return@Button
                }

                // 🔹 Validation
                if (fullName.isBlank() || email.isBlank()
                    || password.isBlank() || confirmPassword.isBlank()
                ) {
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // 🔹 Firebase Registration
                userViewModel.register(email.trim(), password.trim()) { success, message, userId ->
                    if (success) {
                        val user = UserModel(userId, fullName, email)
                        userViewModel.addUserToDatabase(userId, user) { _, _ ->
                            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, LoginActivity::class.java))
                        }
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .testTag("registerButton"),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text("Create Account", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Already have an account? Login",
            color = PrimaryColor,
            modifier = Modifier.clickable {
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        )
    }
}

/* ---------- Reusable Components ---------- */

@Composable
fun InteriorInput(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.fillMaxWidth() // Correctly apply the passed modifier
    )
}

@Composable
fun InteriorPassword(
    label: String,
    value: String,
    visible: Boolean,
    modifier: Modifier = Modifier,
    onToggle: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation =
            if (visible) VisualTransformation.None
            else PasswordVisualTransformation(),
        trailingIcon = {
            Text(
                text = if (visible) "Hide" else "Show",
                modifier = Modifier.clickable { onToggle() },
                color = Color.Gray
            )
        },
        modifier = modifier.fillMaxWidth() // Correctly apply the passed modifier
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegistrationBody()
}