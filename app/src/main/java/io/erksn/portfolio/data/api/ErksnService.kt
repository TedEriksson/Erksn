package io.erksn.portfolio.data.api

import io.erksn.portfolio.data.api.model.ProjectsResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ErksnService {

    @GET("/projects.json")
    fun getProjects(): Deferred<ProjectsResponse>
}