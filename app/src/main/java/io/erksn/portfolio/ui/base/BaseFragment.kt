package io.erksn.portfolio.ui.base

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @MainThread
    inline fun <reified VM : ViewModel> Fragment.injectedViewModels(
        noinline ownerProducer: () -> ViewModelStoreOwner = { this }
    ) = viewModels<VM>(factoryProducer = { viewModelFactory }, ownerProducer = ownerProducer)
}