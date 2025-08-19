package br.com.rodrigoamora.iplist.ui.fragment.devices

import android.content.Context
import androidx.lifecycle.ViewModel
import br.com.rodrigoamora.iplist.model.NetworkDevice
import br.com.rodrigoamora.iplist.model.NetworkWifi
import br.com.rodrigoamora.iplist.util.NetworkUtil

class DevicesListViewModel : ViewModel() {

    fun getDevicesOnTheNetwork(context: Context) : Pair<NetworkWifi?, List<NetworkDevice>> {
        val networkUtil = NetworkUtil(context)
        return networkUtil.detectDevicesOnTheNetwork()
    }

}
