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
import io.erksn.portfolio.viewmodel.switchMap
import javax.inject.Inject

class ProjectDetailViewModel @Inject constructor(private val projectRepository: ProjectRepository) : ViewModel() {

    private val _projectId = MutableLiveData<String>()

    val project: LiveData<Project> = _projectId.switchMap {
       MediatorLiveData<Project>().apply {
           addSource(projectRepository.projectById(it)) {
               it.getOrNull()?.ifPresent {
                   value = it
               }
           }
       }
    }

    fun setProjectId(projectId: String) {
        if (_projectId.value != projectId) {
        }
            _projectId.value = projectId
    }
}

@Module
abstract class ProjectDetailViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProjectDetailViewModel::class)
    abstract fun bindViewModel(viewModel: ProjectDetailViewModel): ViewModel

}