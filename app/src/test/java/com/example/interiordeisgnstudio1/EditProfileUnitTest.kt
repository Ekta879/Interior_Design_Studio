package com.example.interiordeisgnstudio1

import com.example.interiordeisgnstudio1.Model.UserModel
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

class EditProfileUnitTest {

    @Test
    fun editProfile_success_test() {
        val repo: UserRepo = mock()
        val viewModel = UserViewModel(repo)

        val userId = "user123"
        val updatedUser = UserModel(
            // Ensure these field names match your actual UserModel.kt file
            id = userId,
            firstName = "New Name",
            email = "newemail@example.com"
        )

        // Mock repo success callback
        whenever(repo.editProfile(eq(userId), eq(updatedUser), any()))
            .thenAnswer { invocation ->
                val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
                callback(true, "Profile updated")
                null
            }

        var successResult = false
        var messageResult = ""

        // Call editProfile
        viewModel.editProfile(userId, updatedUser) { success, msg ->
            successResult = success
            messageResult = msg
        }

        // Assert success
        assertTrue(successResult)
        assertEquals("Profile updated", messageResult)

        // Verify repo called correctly
        verify(repo).editProfile(eq(userId), eq(updatedUser), any())
    }

    @Test
    fun editProfile_failure_test() {
        val repo: UserRepo = mock()
        val viewModel = UserViewModel(repo)

        val userId = "user123"
        val updatedUser = UserModel(
            id = userId,
            firstName = "New Name",
            email = "newemail@example.com"
        )

        // Mock repo failure callback
        whenever(repo.editProfile(eq(userId), eq(updatedUser), any()))
            .thenAnswer { invocation ->
                val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
                callback(false, "Update failed")
                null
            }

        var successResult = true
        var messageResult = ""

        // Call editProfile
        viewModel.editProfile(userId, updatedUser) { success, msg ->
            successResult = success
            messageResult = msg
        }

        // Assert failure
        assertFalse(successResult)
        assertEquals("Update failed", messageResult)

        // Verify repo called correctly
        verify(repo).editProfile(eq(userId), eq(updatedUser), any())
    }
}