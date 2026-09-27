package com.example.doncontrol.shizuku

import android.content.Context
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

enum class ShizukuStatus {
    CONNECTED,
    DISCONNECTED,
    PERMISSION_REQUIRED,
    UNAVAILABLE
}

class ShizukuManager(private val context: Context) {

    private val PERMISSION_REQUEST_CODE = 1001

    interface Listener {
        fun onStatusChanged(status: ShizukuStatus)
    }

    private var listener: Listener? = null

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        checkStatus()
    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        checkStatus()
    }

    private val permissionResultListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (requestCode == PERMISSION_REQUEST_CODE) {
            checkStatus()
        }
    }

    fun register(listener: Listener) {
        this.listener = listener
        try {
            Shizuku.addBinderReceivedListenerSticky(binderReceivedListener)
            Shizuku.addBinderDeadListener(binderDeadListener)
            Shizuku.addRequestPermissionResultListener(permissionResultListener)
        } catch (e: Exception) {
            Log.e("ShizukuManager", "Error registering Shizuku listeners", e)
        }
        checkStatus()
    }

    fun unregister() {
        try {
            Shizuku.removeBinderReceivedListener(binderReceivedListener)
            Shizuku.removeBinderDeadListener(binderDeadListener)
            Shizuku.removeRequestPermissionResultListener(permissionResultListener)
        } catch (e: Exception) {
            Log.e("ShizukuManager", "Error unregistering Shizuku listeners", e)
        }
        this.listener = null
    }

    fun checkStatus(): ShizukuStatus {
        val status = try {
            if (!Shizuku.pingBinder()) {
                ShizukuStatus.DISCONNECTED
            } else if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                ShizukuStatus.CONNECTED
            } else {
                ShizukuStatus.PERMISSION_REQUIRED
            }
        } catch (e: Exception) {
            ShizukuStatus.UNAVAILABLE
        }
        listener?.onStatusChanged(status)
        return status
    }

    fun requestPermission() {
        try {
            if (Shizuku.pingBinder() && Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(PERMISSION_REQUEST_CODE)
            }
        } catch (e: Exception) {
            Log.e("ShizukuManager", "Error requesting permission", e)
        }
    }

    fun executeShellCommand(command: String): String {
        if (checkStatus() != ShizukuStatus.CONNECTED) {
            return "Shizuku not connected"
        }
        return try {
            val process = Shizuku::class.java.getDeclaredMethod(
                "newProcess",
                Array<String>::class.java,
                Array<String>::class.java,
                String::class.java
            ).invoke(null, arrayOf("sh", "-c", command), null, null) as Process

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            process.waitFor()
            output.toString().trim()
        } catch (e: Exception) {
            Log.e("ShizukuManager", "Failed to execute shell command: $command", e)
            "Error: ${e.message}"
        }
    }

    // System Quick Action Helpers via Shizuku Shell Commands
    fun setWifiEnabled(enabled: Boolean) {
        val cmd = if (enabled) "svc wifi enable" else "svc wifi disable"
        executeShellCommand(cmd)
    }

    fun setBluetoothEnabled(enabled: Boolean) {
        val state = if (enabled) "enable" else "disable"
        executeShellCommand("cmd bluetooth_manager $state")
    }

    fun setFlashlightEnabled(enabled: Boolean) {
        val state = if (enabled) "1" else "0"
        executeShellCommand("cmd statusbar torch $state")
    }

    fun setAirplaneMode(enabled: Boolean) {
        val state = if (enabled) "enable" else "disable"
        executeShellCommand("cmd connectivity set-airplane-mode $state")
    }

    fun setBrightness(percentage: Int) {
        val val255 = (percentage * 2.55).toInt().coerceIn(0, 255)
        executeShellCommand("settings put system screen_brightness $val255")
    }

    fun setVolume(percentage: Int) {
        val streamVal = (percentage * 15 / 100).coerceIn(0, 15)
        executeShellCommand("media volume --stream 3 --set $streamVal")
    }

    fun setDarkTheme(enabled: Boolean) {
        val mode = if (enabled) "yes" else "no"
        executeShellCommand("cmd uimode night $mode")
    }

    fun setAutoRotate(enabled: Boolean) {
        val val01 = if (enabled) "1" else "0"
        executeShellCommand("settings put system accelerometer_rotation $val01")
    }
}