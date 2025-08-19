package br.com.rodrigoamora.iplist.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

import br.com.rodrigoamora.iplist.ui.fragment.devices.DevicesListViewModel
import br.com.rodrigoamora.iplist.ui.fragment.network.NetworkInfoViewModel

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            viewModelModule,
        )
    )
}

val viewModelModule = module {
    viewModel { DevicesListViewModel() }
    viewModel { NetworkInfoViewModel() }
}
