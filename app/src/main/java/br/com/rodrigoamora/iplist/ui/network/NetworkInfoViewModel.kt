package br.com.rodrigoamora.iplist.ui.network

import android.content.Context
import androidx.lifecycle.ViewModel
import br.com.rodrigoamora.iplist.model.NetworkWifi
import br.com.rodrigoamora.iplist.util.NetworkUtil

class NetworkInfoViewModel : ViewModel() {

    fun informationNetwork(context: Context): NetworkWifi? {
        val networkUtil = NetworkUtil(context)
        return networkUtil.informationNetwork()
    }


}