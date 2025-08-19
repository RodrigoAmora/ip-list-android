package br.com.rodrigoamora.iplist.ui.recyclerview.adaper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.rodrigoamora.iplist.R
import br.com.rodrigoamora.iplist.model.NetworkDevice
import br.com.rodrigoamora.iplist.ui.recyclerview.viewholder.DevicesListFragmentViewHolder

class DevicesListAdapter(
    private val context: Context,
    private val deviceList: MutableList<NetworkDevice> = mutableListOf()
) : RecyclerView.Adapter<DevicesListFragmentViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DevicesListFragmentViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.adapter_devices_list, parent, false)
        return DevicesListFragmentViewHolder(view)
    }

    override fun getItemCount(): Int = deviceList.size

    override fun onBindViewHolder(holder: DevicesListFragmentViewHolder, position: Int) {
        val device = deviceList[position]

        holder.setValues(context, device)
    }

    fun update(devices: MutableList<NetworkDevice>) {
        this.deviceList.clear()
        this.deviceList.addAll(devices)

//        diffResult.dispatchUpdatesTo(this)
    }
}
