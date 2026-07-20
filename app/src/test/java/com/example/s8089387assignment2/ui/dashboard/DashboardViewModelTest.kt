package com.example.s8089387assignment2.ui.dashboard

import com.example.s8089387assignment2.data.model.AnimalEntity
import com.example.s8089387assignment2.data.model.DashboardResponse
import com.example.s8089387assignment2.data.repository.DashboardRepository
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
class DashboardViewModelTest {

    // fake repository, so tests don't hit the real network
    private val repository: DashboardRepository = mockk()
    private lateinit var viewModel: DashboardViewModel
    private val testDispatcher = StandardTestDispatcher()

    // a sample entity used across tests, matching the real fields confirmed from the live API
    private val sampleEntity = AnimalEntity(
        species = "African Elephant",
        scientificName = "Loxodonta africana",
        habitat = "Savanna",
        diet = "Herbivore",
        conservationStatus = "Vulnerable",
        averageLifespan = 60,
        description = "The largest land animal, known for its intelligence, social behavior, and distinctive trunk."
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DashboardViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDashboard with valid keypass emits Success state with entities`() = runTest {
        // arrange
        val response = DashboardResponse(entities = listOf(sampleEntity), entityTotal = 1)
        coEvery { repository.getDashboard("animals") } returns response

        // act
        viewModel.loadDashboard("animals")
        testDispatcher.scheduler.advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Success)
        assertEquals(1, (state as DashboardUiState.Success).entities.size)
        assertEquals("African Elephant", state.entities[0].species)
    }

    @Test
    fun `loadDashboard with repository failure emits Error state`() = runTest {
        // arrange
        coEvery { repository.getDashboard(any()) } throws Exception("Network error")

        // act
        viewModel.loadDashboard("animals")
        testDispatcher.scheduler.advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Error)
        assertEquals("Network error", (state as DashboardUiState.Error).message)
    }

    @Test
    fun `loadDashboard sets Loading state before the result arrives`() = runTest {
        // arrange: dispatcher is paused, so the coroutine won't run to completion yet
        val response = DashboardResponse(entities = listOf(sampleEntity), entityTotal = 1)
        coEvery { repository.getDashboard("animals") } returns response

        // act: call but don't advance the dispatcher yet
        viewModel.loadDashboard("animals")

        // assert: state should be Loading immediately after the call, before the coroutine resolves
        assertTrue(viewModel.uiState.value is DashboardUiState.Loading)

        // cleanup: let the coroutine finish so it doesn't leak into other tests
        testDispatcher.scheduler.advanceUntilIdle()
    }
}