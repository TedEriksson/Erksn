package io.erksn.portfolio.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import io.erksn.portfolio.ErksnApplication
import io.erksn.portfolio.data.api.ApiModule
import io.erksn.portfolio.ui.main.MainActivityModule
import io.erksn.portfolio.viewmodel.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        MainActivityModule::class,
        ViewModelModule::class,
        ApiModule::class
    ]
)
interface AppComponent : AndroidInjector<ErksnApplication> {
    @dagger.Component.Builder
    abstract class Builder : AndroidInjector.Builder<ErksnApplication>()
}