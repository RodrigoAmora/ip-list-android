package br.com.rodrigoamora.iplist.model

data class NetworkDevice(
    val hostname: String,
    val ipAddress: String,
    val isReachable: Boolean,
    val macAddress: String?,
    val typeDevice: TypeDevice,
    val manufacturer: String?
)
