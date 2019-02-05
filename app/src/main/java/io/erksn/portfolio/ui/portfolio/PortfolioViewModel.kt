package io.erksn.portfolio.ui.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.erksn.portfolio.di.ViewModelKey
import io.erksn.portfolio.data.api.model.Project
import io.erksn.portfolio.repository.ProjectRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PortfolioViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    val state: LiveData<PortfolioState> = MediatorLiveData<PortfolioState>().apply {
        value = PortfolioState.Loading
        addSource(projectRepository.projects()) {
            value = it.fold(
                onSuccess = {
                    PortfolioState.Data(it) as PortfolioState
                },
                onFailure = {
                    when(it) {
                        is IOException -> PortfolioState.Error.Retry
                        else -> PortfolioState.Error.Bail
                    }
                }
            )
        }
    }

    fun retryProjects() {
        projectRepository.refreshProjects()
    }
}

sealed class PortfolioState {
    object Loading: PortfolioState()
    class Data(val projects: List<Project>): PortfolioState()
    sealed class Error : PortfolioState() {
        object Retry: Error()
        object Bail: Error()
    }
}

@Module
abstract class PortfolioViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PortfolioViewModel::class)
    abstract fun bindViewModel(viewModel: PortfolioViewModel): ViewModel

}