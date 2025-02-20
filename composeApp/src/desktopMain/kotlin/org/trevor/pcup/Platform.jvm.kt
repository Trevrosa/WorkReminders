package org.trevor.pcup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import co.touchlab.kermit.Logger

// @Composable unused here, required because of expected function
@Composable
actual fun getPlatform(): Platform = JVMPlatform()

@Composable
@NonRestartableComposable
actual fun init() {
    Logger.i("init", tag = "PlatformInit")
}

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val battery: Int? = null

    /**
     * Not planned to be implemented for this platform.
     */
    override fun getScreenTimeData(): List<ScreenTime>? = null
    @Composable
    override fun sendNotification(message: String) {
        Logger.e("Not yet implemented", tag = "sendNotification")
    }
}

@Composable
actual fun Graph(data: Collection<Number>) {
    Logger.e("Not yet implemented", tag = "Graph")
}
