package com.example.interiordeisgnstudio1

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interiordeisgnstudio1.ui.theme.AccentBrown
import com.example.interiordeisgnstudio1.ui.theme.SoftPink

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

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    // ☕ SmartBite Brown Palette
    val Espresso = Color(0xFF3E2723)   // Dark Brown
    val Mocha = Color(0xFF886565)      // Medium Brown
    val Softpink = Color(0xFF886565)    // Soft Pink
    val Cream = Color(0xFFFFF3E0)      // Cream background
    val Daisy = Color(0xFF6A5ACD) // Same highlight


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "INTERIOR DESIGN",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Espresso
        )

        Text(
            text = "Spaces that inspires",
            fontSize = 14.sp,
            color = Softpink
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Infusing life into spaces with vibrant colors and textures",
            fontSize = 14.sp,
            color = Espresso.copy(alpha = 0.7f),
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 30.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

        InteriorInput("Full Name", fullName, onValueChange = { fullName = it }, borderColor = Mocha)
        Spacer(modifier = Modifier.height(14.dp))

        InteriorInput("Email Address", email, keyboardType = KeyboardType.Email, onValueChange = { email = it }, borderColor = Mocha)
        Spacer(modifier = Modifier.height(14.dp))

        InteriorPassword(
            label = "Password",
            value = password,
            visible = passwordVisibility,
            onToggle = { passwordVisibility = !passwordVisibility },
            onValueChange = { password = it },
            borderColor = Mocha
        )
        Spacer(modifier = Modifier.height(14.dp))

        InteriorPassword(
            label = "Confirm Password",
            value = confirmPassword,
            visible = confirmPasswordVisibility,
            onToggle = { confirmPasswordVisibility = !confirmPasswordVisibility },
            onValueChange = { confirmPassword = it },
            borderColor = Mocha
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                userViewModel.register(email.trim(), password.trim()) { success, message, userId ->
                    if (success) {
                        val user = UserModel(
                            id = userId,
                            firstName = fullName.trim(),
                            email = email.trim()
                        )
                        userViewModel.addUserToDatabase(userId, user) { dbSuccess, dbMessage ->
                            Toast.makeText(context, dbMessage, Toast.LENGTH_SHORT).show()
                            if (dbSuccess) {
                                context.startActivity(
                                    Intent(context, LoginActivity::class.java)
                                )
                            }
                        }
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentBrown)
        ) {
            Text(
                text = "Create Account",
                fontSize = 16.sp,
                color = Cream
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Already a member? Login",
            fontSize = 14.sp,
            color = Softpink,
            modifier = Modifier.clickable {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
        )
    }
}

// ----------------- Custom Inputs -----------------
@Composable
fun InteriorInput(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    borderColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = borderColor) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = borderColor,
            unfocusedIndicatorColor = borderColor,
            focusedTextColor = borderColor,
            unfocusedTextColor = borderColor
        )
    )
}

@Composable
fun InteriorPassword(
    label: String,
    value: String,
    visible: Boolean,
    onToggle: () -> Unit,
    onValueChange: (String) -> Unit,
    borderColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = borderColor) },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggle) {
                Icon(
                    painter = if (visible) painterResource(R.drawable.baseline_visibility_off_24)
                    else painterResource(R.drawable.baseline_visibility_24),
                    contentDescription = null,
                    tint = borderColor
                )
            }
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = borderColor,
            unfocusedIndicatorColor = borderColor,
            focusedTextColor = borderColor,
            unfocusedTextColor = borderColor
        )
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegistrationBody()
}