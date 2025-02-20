package org.trevor.pcup

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        Logger.setTag("MainActivity")

        // POST_NOTIFICATIONS requires sdk >= 33 (tiramisu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission =
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            Logger.d("we have notification permissions? ${notificationPermission == PackageManager.PERMISSION_GRANTED}")

            if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
                val launcher = registerForActivityResult(RequestPermission()) {
                    Logger.d("request permission result: $it")
                }
                Logger.d("launching system dialog to request for permission")
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}