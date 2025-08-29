/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.connecteddevicebluetoothlister.presentation

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.example.connecteddevicebluetoothlister.R


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onResume() {
        super.onResume()
        getBatteryLevel()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun getBatteryLevel() {
        var batteryLevels = ""
        val bAdapter = BluetoothAdapter.getDefaultAdapter()
        // Checks if Bluetooth Adapter is present
        if (bAdapter == null) {
            Toast.makeText(applicationContext, "Bluetooth Not Supported", Toast.LENGTH_SHORT).show()
            return
        } else {
            // Arraylist of all the bonded (paired) devices
            val pairedDevices = bAdapter.bondedDevices
            if (pairedDevices.isNotEmpty()) {
                for (device in pairedDevices) {
                    val batteryLevel = device.javaClass.getMethod("getBatteryLevel").invoke(device) as Int
                    batteryLevels = batteryLevels + "device.name: " + device.name + " battery.level: ~ " + batteryLevel + " %, "
                }
            } else {
                batteryLevels = "No paired devices found."
            }
        }
        val textView = findViewById<View>(R.id.textView) as TextView
        textView.text = batteryLevels.trimEnd(',', ' ')
    }

}