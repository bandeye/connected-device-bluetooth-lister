/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.connecteddevicebluetoothlister.presentation

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresPermission
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.connecteddevicebluetoothlister.R
import com.example.connecteddevicebluetoothlister.presentation.theme.ConnectedDeviceBluetoothListerTheme


class MainActivity : ComponentActivity() {
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        getBatteryLevel()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun getBatteryLevel() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)

        val bAdapter = BluetoothAdapter.getDefaultAdapter()
        // Checks if Bluetooth Adapter is present
        if (bAdapter == null) {
            Toast.makeText(applicationContext, "Bluetooth Not Supported", Toast.LENGTH_SHORT).show()
        } else {
            // Arraylist of all the bonded (paired) devices
            val pairedDevices = bAdapter.bondedDevices
            if (pairedDevices.size > 0) {
                for (device in pairedDevices) {
                    val batteryLevel = device.javaClass.getMethod("getBatteryLevel").invoke(device) as Int

                    setContent {
                        WearApp(device.name + " " + batteryLevel)
                    }
                }
            }
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    ConnectedDeviceBluetoothListerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}