package io.erksn.portfolio

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.erksn.portfolio.di.DaggerAppComponent

class ErksnApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .create(this)
    }
}