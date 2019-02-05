package io.erksn.portfolio.viewmodel

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import io.erksn.portfolio.ui.portfolio.PortfolioViewModelModule
import io.erksn.portfolio.ui.project.ProjectDetailViewModelModule

@Module(
    includes = [
        ProjectDetailViewModelModule::class,
        PortfolioViewModelModule::class
    ]
)
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ErksnViewModelFactory): ViewModelProvider.Factory
}