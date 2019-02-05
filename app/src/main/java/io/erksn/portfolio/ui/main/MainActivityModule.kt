package io.erksn.portfolio.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.erksn.portfolio.ui.portfolio.PortfolioFragment
import io.erksn.portfolio.ui.project.ProjectDetailFragment

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [MainFragmentsModule::class])
    abstract fun contributeMainActivity(): MainActivity
}

@Module
abstract class MainFragmentsModule {

    @ContributesAndroidInjector
    abstract fun contributesPortfolioFragment(): PortfolioFragment

    @ContributesAndroidInjector
    abstract fun contributesProjectDetailFragment(): ProjectDetailFragment

}