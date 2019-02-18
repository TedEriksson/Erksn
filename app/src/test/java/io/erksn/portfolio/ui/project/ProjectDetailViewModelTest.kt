package io.erksn.portfolio.ui.project

import com.nhaarman.mockitokotlin2.*
import io.erksn.portfolio.data.api.model.Project
import io.erksn.portfolio.repository.ProjectRepository
import io.erksn.portfolio.repository.ProjectResult
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ProjectDetailViewModelTest : CoroutinesTest() {

    val projectRepository = mock<ProjectRepository>()

    val viewModel = ProjectDetailViewModel(projectRepository)

    private val testProject = Project("testId", "test", "test", "#FFFFFF", "#FFFFFF")

    @Test
    fun `when project exists then show project`() = runBlocking {
        // Given
        val observer = viewModel.project.observeForTest()
        whenever(projectRepository.projectById("testId")).thenReturn(ProjectResult.Success(testProject))

        // When
        viewModel.setProjectId("testId")

        // Then
        verify(observer).onChanged(testProject)
    }

    @Test
    fun `when project is missing then do nothing`() = runBlocking {
        // Given
        val observer = viewModel.project.observeForTest()
        whenever(projectRepository.projectById("testId")).thenReturn(ProjectResult.Success(null))

        // When
        viewModel.setProjectId("testId")

        // Then
        verify(observer, times(0)).onChanged(any())
    }

    @Test
    fun `when project has failure then do nothing`() = runBlocking {
        // Given
        val observer = viewModel.project.observeForTest()
        whenever(projectRepository.projectById("testId")).thenReturn(ProjectResult.Failure(canRetry = false))

        // When
        viewModel.setProjectId("testId")

        // Then
        verify(observer, times(0)).onChanged(any())
    }
}