package io.erksn.portfolio.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.erksn.portfolio.data.api.ErksnService
import io.erksn.portfolio.data.api.model.Project
import io.erksn.portfolio.viewmodel.map
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class ProjectRepository @Inject constructor(private val erksnService: ErksnService) : CoroutineScope {

    private var inflightRequest: Job? = null

    private val projects = MutableLiveData<Result<List<Project>>>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    fun projectById(projectId: String): LiveData<Result<Optional<Project>>> {
        return projects.map { projectList ->
            projectList.fold(
                onSuccess =  { list ->
                    Result.success(Optional.ofNullable(list.find { it.id == projectId }))
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        }.also {
            startRequestIfNotRunning()
        }
    }

    fun projects(): LiveData<Result<List<Project>>> {
        return projects.also {
            startRequestIfNotRunning()
        }
    }

    fun refreshProjects() {
        startNewRequest()
    }

    private fun startRequestIfNotRunning() {
        if (inflightRequest == null) startNewRequest()
    }

    private fun startNewRequest() = launch {
        inflightRequest?.cancelAndJoin()
        inflightRequest = this@ProjectRepository.launch {
            projects.value = try {
                Result.success(erksnService.getProjects().await().projects)
            } catch (e: Exception) {
                when (e) {
                    is IOException, is HttpException -> Result.failure(e)
                    else -> throw e
                }
            }
        }
    }

}

