package br.com.rodrigoamora.iplist.ui.recyclerview.viewholder

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.rodrigoamora.iplist.R
import br.com.rodrigoamora.iplist.model.NetworkDevice

class DevicesListFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var tvDeviceIP : TextView

    fun setValues(context: Context, deivce: NetworkDevice) {
        tvDeviceIP = itemView.findViewById(R.id.tv_device_ip)
        tvDeviceIP.text = deivce.ipAddress
    }

}
