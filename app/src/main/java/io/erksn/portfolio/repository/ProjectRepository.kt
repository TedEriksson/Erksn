package io.erksn.portfolio.repository

import io.erksn.portfolio.data.api.ErksnService
import io.erksn.portfolio.data.api.model.Project
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class ProjectRepository @Inject constructor(private val erksnService: ErksnService) : CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private var projects: Deferred<ProjectResult<List<Project>>> = createProjectRequestAsync()

    suspend fun projectById(projectId: String): ProjectResult<Project?> {
        return when(val result = projects.await()) {
            is ProjectResult.Success -> ProjectResult.Success(result.data.find { it.id == projectId })
            is ProjectResult.Failure -> ProjectResult.Failure(result.canRetry)
        }
    }

    suspend fun projects(): ProjectResult<List<Project>> {
        return projects.await()
    }

    fun clearProjects() {
        projects = createProjectRequestAsync()
    }

    private fun createProjectRequestAsync(): Deferred<ProjectResult<List<Project>>> = async(start = CoroutineStart.LAZY) {
        try {
            ProjectResult.Success(erksnService.getProjects().await().projects)
        } catch (e: Exception) {
            when (e) {
                is IOException -> ProjectResult.Failure<List<Project>>(canRetry = true)
                is HttpException -> ProjectResult.Failure(canRetry = false)
                else -> throw e
            }
        }
    }
}

sealed class ProjectResult<T> {
    data class Success<T>(val data: T) : ProjectResult<T>()
    data class Failure<T>(val canRetry: Boolean) : ProjectResult<T>()
}

