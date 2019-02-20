package io.erksn.portfolio.ui.portfolio

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
import io.erksn.portfolio.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PortfolioViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : BaseViewModel() {

    init {
        retryProjects()
    }

    private val _projects = MutableLiveData<List<Project>>()
    private val _errors = MutableLiveData<PortfolioState.Error>()

    val data: LiveData<PortfolioState.Data> = MediatorLiveData<PortfolioState.Data>().apply {
        value = PortfolioState.Data.Loading
        addSource(_projects) {
            value = PortfolioState.Data.Done(it)
        }
    }

    val errors: LiveData<PortfolioState.Error>
        get() = _errors

    fun retryProjects() {
        launch {
            projectRepository.clearProjects()
            when (val result = projectRepository.projects()) {
                is ProjectResult.Success -> _projects.postValue(result.data)
                is ProjectResult.Failure -> {
                    _projects.postValue(emptyList())
                    _errors.postValue(if (result.canRetry) PortfolioState.Error.Retry() else PortfolioState.Error.Bail())
                }
            }
        }
    }
}

sealed class PortfolioState {
    sealed class Data {
        object Loading : Data()
        class Done(val projects: List<Project>) : Data()
    }

    sealed class Error : PortfolioState() {
        var isHandled = false
            private set

        fun handle() {
            isHandled = true
        }

        class Retry : Error()
        class Bail : Error()
    }
}

@Module
abstract class PortfolioViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PortfolioViewModel::class)
    abstract fun bindViewModel(viewModel: PortfolioViewModel): ViewModel

}