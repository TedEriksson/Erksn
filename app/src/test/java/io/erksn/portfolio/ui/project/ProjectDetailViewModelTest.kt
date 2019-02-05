package io.erksn.portfolio.ui.project

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.erksn.portfolio.data.api.model.Project
import io.erksn.portfolio.repository.ProjectRepository
import io.erksn.portfolio.util.asLiveData
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException
import java.lang.Exception
import java.util.*

class ProjectDetailViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val projectRepository = mock<ProjectRepository>()

    val viewModel = ProjectDetailViewModel(projectRepository)

    private val testProject = Project("testId", "test", "test", "#FFFFFF", "#FFFFFF")

    @Test
    fun `when project exists then show project`() {
        // Given
        val observer = viewModel.project.observeForTest()
        whenever(projectRepository.projectById("testId")).thenReturn(Result.success(Optional.of(testProject)).asLiveData())

        // When
        viewModel.setProjectId("testId")

        // Then
        verify(observer).onChanged(testProject)
    }

    @Test
    fun `when project is missing then do nothing`() {
        // Given
        val observer = viewModel.project.observeForTest()
        whenever(projectRepository.projectById("testId")).thenReturn(Result.success(Optional.empty<Project>()).asLiveData())

        // When
        viewModel.setProjectId("testId")

        // Then
        verify(observer, times(0)).onChanged(any())
    }

    @Test
    fun `when project has failure then do nothing`() {
        // Given
        val observer = viewModel.project.observeForTest()
        whenever(projectRepository.projectById("testId")).thenReturn(Result.failure<Optional<Project>>(Exception()).asLiveData())

        // When
        viewModel.setProjectId("testId")

        // Then
        verify(observer, times(0)).onChanged(any())
    }


    fun <T> LiveData<T>.observeForTest(): Observer<T> {
        return mock<Observer<T>>().also {
            observeForever(it)
        }
    }
}