package com.example.s8089387assignment2.ui.login

import com.example.s8089387assignment2.data.model.LoginResponse
import com.example.s8089387assignment2.data.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // fake repository, so tests don't hit the real network
    private val repository: LoginRepository = mockk()
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // ViewModel's viewModelScope needs a Main dispatcher; swap in a test-controlled one
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with valid credentials emits Success state with keypass`() = runTest {
        // arrange: repository returns a successful response
        coEvery { repository.login("s8089387", "Fredrick") } returns LoginResponse(keypass = "animals")

        // act
        viewModel.login("s8089387", "Fredrick")
        testDispatcher.scheduler.advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Success)
        assertEquals("animals", (state as LoginUiState.Success).keypass)
    }

    @Test
    fun `login with repository failure emits Error state`() = runTest {
        // arrange: repository throws
        coEvery { repository.login(any(), any()) } throws Exception("Invalid credentials")

        // act
        viewModel.login("wronguser", "wrongpass")
        testDispatcher.scheduler.advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("Invalid credentials", (state as LoginUiState.Error).message)
    }

    @Test
    fun `login with blank username emits Error state without calling repository`() = runTest {
        // act
        viewModel.login("", "somepassword")
        testDispatcher.scheduler.advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
    }
}