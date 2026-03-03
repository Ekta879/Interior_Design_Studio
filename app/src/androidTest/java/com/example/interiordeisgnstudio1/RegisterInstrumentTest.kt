package com.example.interiordeisgnstudio1

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.Intents.intended
import com.example.interiordeisgnstudio1.view.LoginActivity
import com.example.interiordeisgnstudio1.view.RegistrationActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterInstrumentTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<RegistrationActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testRegistrationNavigatesToLogin() {
        // Generate a unique email to avoid "email already in use" errors
        val uniqueEmail = "testuser${System.currentTimeMillis()}@example.com"

        composeRule.onNodeWithTag("fullName")
            .performTextInput("Anju Thapa")

        composeRule.onNodeWithTag("email")
            .performTextInput(uniqueEmail)

        composeRule.onNodeWithTag("password")
            .performTextInput("Anju123")

        composeRule.onNodeWithTag("confirmPassword")
            .performTextInput("Anju123")

        composeRule.onNodeWithTag("registerButton")
            .performClick()

        // Wait for the Intent to be recorded (max 10 seconds)
        var success = false
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < 10000) {
            try {
                intended(hasComponent(LoginActivity::class.java.name))
                success = true
                break
            } catch (e: Exception) {
                Thread.sleep(500)
            }
        }

        if (!success) {
            throw AssertionError("Navigation to LoginActivity failed. Registration might have failed or taken too long.")
        }
    }
}
