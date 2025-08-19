package br.com.rodrigoamora.iplist.model

data class NetworkWifi(
    val ssid: String,
    val bssid: String?,
    val strength: Int?, // em dBm
    val frequency: Int?, // em MHz
    val linkSpeed: Int?, // em Mbps
    val networkId: Int?,
    val macAddress: String?,
    val ipAddress: Int?
)
