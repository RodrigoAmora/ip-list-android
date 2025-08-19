package br.com.rodrigoamora.iplist.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.core.app.ActivityCompat
import br.com.rodrigoamora.iplist.model.NetworkDevice
import br.com.rodrigoamora.iplist.model.NetworkWifi
import br.com.rodrigoamora.iplist.model.TypeDevice
import java.net.InetAddress
import java.net.NetworkInterface
import kotlin.concurrent.thread

class NetworkUtil(private val context: Context) {
    private val wifiManager: WifiManager by lazy {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }


    private fun identifyManufacturer(macAddress: String?): String? {
        if (macAddress == null) return null

        return when {
            macAddress.startsWith("00:0C:E7") -> "Nokia"
            macAddress.startsWith("00:17:AB") -> "Nintendo"
            macAddress.startsWith("00:25:00") -> "Apple"
            macAddress.startsWith("18:E7:F4") -> "Samsung"
            macAddress.startsWith("00:1A:11") -> "Google"
            macAddress.startsWith("00:12:17") -> "Cisco"
            macAddress.startsWith("74:D4:35") -> "Xiaomi"
            else -> "Unknown"
        }
    }

    private fun identifyTypeDevice(macAddress: String?, hostname: String): TypeDevice {
        if (macAddress == null) return TypeDevice.UNKNOWN

        val hostnameLC = hostname.lowercase()
        when {
            hostnameLC.contains("iphone") || hostnameLC.contains("android") ||
                    hostnameLC.contains("smartphone") -> return TypeDevice.SMARTPHONE

            hostnameLC.contains("printer") || hostnameLC.contains("impressora") ->
                return TypeDevice.PRINTER

            hostnameLC.contains("tv") || hostnameLC.contains("samsung") ||
                    hostnameLC.contains("lg") -> return TypeDevice.SMART_TV

            hostnameLC.contains("playstation") || hostnameLC.contains("xbox") ||
                    hostnameLC.contains("nintendo") -> return TypeDevice.CONSOLE_GAME

            hostnameLC.contains("desktop") || hostnameLC.contains("laptop") ||
                    hostnameLC.contains("pc") -> return TypeDevice.COMPUTER
        }

        return when {
            macAddress.startsWith("00:25:00") || // Apple
                    macAddress.startsWith("18:E7:F4") || // Samsung
                    macAddress.startsWith("74:D4:35")    // Xiaomi
                -> TypeDevice.SMARTPHONE

            macAddress.startsWith("00:17:AB") // Nintendo
                -> TypeDevice.CONSOLE_GAME

            macAddress.startsWith("00:12:17") // Cisco
                -> TypeDevice.IOT_DEVICE

            else -> TypeDevice.UNKNOWN
        }
    }

    fun informationNetwork(): NetworkWifi? {
        if (!hasRequiredPermissions()) {
            return NetworkWifi(
                ssid = "Permissões não concedidas",
                bssid = null,
                strength = null,
                frequency = null,
                linkSpeed = null,
                networkId = null,
                macAddress = null,
                ipAddress = null
            )
        }

        try {
            // Verifica se o Wi-Fi está habilitado
            if (!wifiManager.isWifiEnabled) {
                return NetworkWifi(
                    ssid = "Wi-Fi desativado",
                    bssid = null,
                    strength = null,
                    frequency = null,
                    linkSpeed = null,
                    networkId = null,
                    macAddress = null,
                    ipAddress = null
                )
            }

            // Obtém informações da conexão atual
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12 (API 31) e superior
                val networkInfo = context.getSystemService(WifiManager::class.java)
                val wifiInfo = networkInfo.connectionInfo
                wifiInfo.ipAddress
                NetworkWifi(
                    ssid = wifiInfo.ssid.removeSurrounding("\""),
                    bssid = wifiInfo.bssid,
                    strength = wifiInfo.rssi,
                    frequency = wifiInfo.frequency,
                    linkSpeed = wifiInfo.linkSpeed,
                    networkId = wifiInfo.networkId,
                    macAddress = wifiInfo.macAddress,
                    ipAddress = wifiInfo.ipAddress
                )
            } else {
                // Para versões anteriores ao Android 12
                @Suppress("DEPRECATION")
                val wifiInfo = wifiManager.connectionInfo

                NetworkWifi(
                    ssid = wifiInfo.ssid.removeSurrounding("\""),
                    bssid = wifiInfo.bssid,
                    strength = wifiInfo.rssi,
                    frequency = wifiInfo.frequency,
                    linkSpeed = wifiInfo.linkSpeed,
                    networkId = wifiInfo.networkId,
                    macAddress = wifiInfo.macAddress,
                    ipAddress = wifiInfo.ipAddress
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkWifi(
                ssid = "Erro: ${e.message}",
                bssid = null,
                strength = null,
                frequency = null,
                linkSpeed = null,
                networkId = null,
                macAddress = null,
                ipAddress = null
            )
        }
    }

    fun detectDevicesOnTheNetwork(timeout: Int = 1000): Pair<NetworkWifi?, List<NetworkDevice>> {
        val devices = mutableListOf<NetworkDevice>()
        val networkWifi = informationNetwork()

        NetworkInterface.getNetworkInterfaces().asSequence()
            .filter { it.isUp && !it.isLoopback }
            .forEach { networkInterface ->
                networkInterface.inetAddresses.asSequence()
                    .filter { it.hostAddress.contains(".") }
                    .forEach { address ->
                        val baseIP = address.hostAddress.substringBeforeLast(".")

                        val threads = (1..254).map { i ->
                            thread {
                                val ipAtual = "$baseIP.$i"
                                val inetAddress = InetAddress.getByName(ipAtual)

                                if (inetAddress.isReachable(timeout)) {
                                    val macAddress = try {
                                        NetworkInterface.getByInetAddress(inetAddress)
                                            ?.hardwareAddress
                                            ?.joinToString(":") { "%02X".format(it) }
                                    } catch (e: Exception) {
                                        null
                                    }

                                    val typeDevice = identifyTypeDevice(macAddress, inetAddress.hostName)
                                    val manufacturer = identifyManufacturer(macAddress)

                                    synchronized(devices) {
                                        devices.add(
                                            NetworkDevice(
                                                hostname = inetAddress.hostName,
                                                ipAddress = ipAtual,
                                                isReachable = true,
                                                macAddress = macAddress,
                                                typeDevice = typeDevice,
                                                manufacturer = manufacturer
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        threads.forEach { it.join() }
                    }
            }

        return Pair(networkWifi, devices.sortedBy { it.ipAddress })
    }

    fun calculateSignalLevel(rssi: Int): Int {
        return WifiManager.calculateSignalLevel(rssi, 100)
    }

    private fun hasRequiredPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 e superior
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.NEARBY_WIFI_DEVICES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Android 12 e inferior
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        }
    }

}
