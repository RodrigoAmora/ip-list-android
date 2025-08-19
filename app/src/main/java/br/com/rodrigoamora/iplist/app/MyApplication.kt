package br.com.rodrigoamora.iplist.app

import android.app.Application
import br.com.rodrigoamora.iplist.di.injectFeature
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instantiateKoin()
    }

    override fun onTerminate() {
        super.onTerminate()
        terminateKoin()
    }

    private fun instantiateKoin() {
        startKoin{
            androidLogger()
            androidContext(this@MyApplication)
            injectFeature()
        }
    }

    private fun terminateKoin() {
        stopKoin()
    }

}
