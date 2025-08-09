package br.com.rodrigoamora.iplist.ui.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.rodrigoamora.iplist.databinding.FragmentNetworkinfoBinding

class NetworkInfoFragment : Fragment() {

    private var _binding: FragmentNetworkinfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var textViewNetworkNameValue: TextView

    private lateinit var networkInfoViewModel: NetworkInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        networkInfoViewModel = ViewModelProvider(this).get(NetworkInfoViewModel::class.java)

        _binding = FragmentNetworkinfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        textViewNetworkNameValue = binding.textNetworkNameValue

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContext = activity?.applicationContext
        appContext?.let {
            val networkUtil = networkInfoViewModel.informationNetwork(it)
            textViewNetworkNameValue.text = networkUtil?.ssid
        }
    }
}