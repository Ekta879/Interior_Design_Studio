package com.example.interiordeisgnstudio1

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.interiordeisgnstudio1.view.ClientDashboardActivity
import com.example.interiordeisgnstudio1.view.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInstrumentTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<LoginActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testLoginNavigatesToDashboard() {
        // 1. Enter Credentials
        // Ensure your LoginBody() uses Modifier.testTag("email"), etc.
        composeRule.onNodeWithTag("email").performTextInput("anjuthapa11@gmail.com")
        composeRule.onNodeWithTag("password").performTextInput("Anju123")

        // 2. Click Login
        composeRule.onNodeWithTag("login").performClick()

        // 3. Verify Intent
        // Give Firebase time to process. Espresso intended() will wait
        // internally for a short time, but for Firebase, we check explicitly.

        // Use a slight delay or better yet, verify a node on the destination screen
        // exists before checking the intent.

        val dashboardClassName = ClientDashboardActivity::class.java.name

        // This is the most reliable way to check for Activity transition in Compose
        intended(hasComponent(dashboardClassName))
    }
}