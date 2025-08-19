package br.com.rodrigoamora.iplist.ui.fragment.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.rodrigoamora.iplist.databinding.FragmentDevicesListBinding
import br.com.rodrigoamora.iplist.model.NetworkDevice
import br.com.rodrigoamora.iplist.ui.activity.MainActivity
import br.com.rodrigoamora.iplist.ui.recyclerview.adaper.DevicesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DevicesListFragment : Fragment() {

    private var _binding: FragmentDevicesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerViewDevices: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: DevicesListAdapter

    private var devicesList: MutableList<NetworkDevice> = mutableListOf()

    private val devicesListViewModel: DevicesListViewModel by viewModel()

    private val mainActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDevicesListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerViewDevices = binding.listDevices
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.configureRecyclerView()
        this.getDevices()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }

    private fun configureRecyclerView() {
        val linearLayout = LinearLayoutManager(this.mainActivity,
                                               LinearLayoutManager.VERTICAL,
                                  false)
        linearLayout.scrollToPosition(0)

        val dividerItemDecoration = DividerItemDecoration(this.mainActivity,
            DividerItemDecoration.VERTICAL)

        this.adapter = DevicesListAdapter(this.mainActivity.applicationContext)

        this.recyclerViewDevices.adapter = this.adapter
        this.recyclerViewDevices.addItemDecoration(dividerItemDecoration)
        this.recyclerViewDevices.setHasFixedSize(true)
        this.recyclerViewDevices.itemAnimator = DefaultItemAnimator()
        this.recyclerViewDevices.layoutManager = linearLayout
        this.recyclerViewDevices.isNestedScrollingEnabled = true
        this.recyclerViewDevices.scrollToPosition(adapter.itemCount - 1)
    }

    private fun populateRecyclerView() {
        this.adapter.update(devicesList)
    }

    private fun getDevices() {
        devicesList = devicesListViewModel.getDevicesOnTheNetwork(mainActivity).second.toMutableList()
        populateRecyclerView()
    }
}
