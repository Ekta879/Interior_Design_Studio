package com.example.interiordeisgnstudio1

import com.example.interiordeisgnstudio1.repository.UserRepo
import com.example.interiordeisgnstudio1.viewmodel.UserViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RegistrationUnitTest {

    @Test
    fun register_success_test() {
        val repo: UserRepo = mock()
        val viewModel = UserViewModel(repo)

        // Mock success callback
        whenever(repo.register(eq("newuser@gmail.com"), eq("password123"), any()))
            .thenAnswer { invocation ->
                val callback = invocation.getArgument<(Boolean, String, String) -> Unit>(2)
                callback(true, "Registered successfully", "userId123")
                null
            }

        var successResult = false
        var messageResult = ""
        var userIdResult = ""

        // Call register
        viewModel.register("newuser@gmail.com", "password123") { success, msg, userId ->
            successResult = success
            messageResult = msg
            userIdResult = userId
        }

        // Verify results
        assertTrue(successResult)
        assertEquals("Registered successfully", messageResult)
        assertEquals("userId123", userIdResult)

        // Verify repo called with correct args
        verify(repo).register(eq("newuser@gmail.com"), eq("password123"), any())
    }

    @Test
    fun register_failure_test() {
        val repo: UserRepo = mock()
        val viewModel = UserViewModel(repo)

        // Mock failure callback
        whenever(repo.register(eq("existinguser@gmail.com"), eq("password123"), any()))
            .thenAnswer { invocation ->
                val callback = invocation.getArgument<(Boolean, String, String) -> Unit>(2)
                callback(false, "Email already exists", "")
                null
            }

        var successResult = true
        var messageResult = ""
        var userIdResult = "nonempty"

        // Call register
        viewModel.register("existinguser@gmail.com", "password123") { success, msg, userId ->
            successResult = success
            messageResult = msg
            userIdResult = userId
        }

        // Verify results
        assertFalse(successResult)
        assertEquals("Email already exists", messageResult)
        assertEquals("", userIdResult)

        // Verify repo called with correct args
        verify(repo).register(eq("existinguser@gmail.com"), eq("password123"), any())
    }
}