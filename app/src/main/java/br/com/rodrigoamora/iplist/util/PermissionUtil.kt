package br.com.rodrigoamora.iplist.util

import android.app.Activity
import androidx.core.app.ActivityCompat

class PermissionUtil {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 200

        fun requestPermissions(activity: Activity, permissions: List<String>) {
            ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }

        fun requestPermission(activity: Activity, permission: String) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_REQUEST_CODE)
        }
    }
}
