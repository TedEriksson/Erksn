package io.erksn.portfolio.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.erksn.portfolio.di.ViewModelKey
import io.erksn.portfolio.data.api.model.Project
import io.erksn.portfolio.repository.ProjectRepository
import io.erksn.portfolio.repository.ProjectResult
import io.erksn.portfolio.viewmodel.switchMap
import kotlinx.coroutines.*
import javax.inject.Inject

class ProjectDetailViewModel @Inject constructor(private val projectRepository: ProjectRepository) : ViewModel(),
    CoroutineScope by MainScope() {

    private val _projectId = MutableLiveData<String>()

    private var job: Job? = null

    val project: LiveData<Project> = _projectId.switchMap {
        job?.cancel()
        MediatorLiveData<Project>().apply {
            job = launch(Dispatchers.Main) {
                when (val result = projectRepository.projectById(it)) {
                    is ProjectResult.Success -> result.data?.let { value = it }
                    is ProjectResult.Failure -> {} // TODO add error handling
                }
            }
        }
    }

    fun setProjectId(projectId: String) {
        if (_projectId.value != projectId) {
            _projectId.value = projectId
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}

@Module
abstract class ProjectDetailViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProjectDetailViewModel::class)
    abstract fun bindViewModel(viewModel: ProjectDetailViewModel): ViewModel

}