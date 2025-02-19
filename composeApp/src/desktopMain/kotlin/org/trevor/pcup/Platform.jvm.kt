package org.trevor.pcup

import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger

// @Composable unused here, required because of expected function
@Composable
actual fun getPlatform(): Platform = JVMPlatform()

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val battery: Int? = null

    /**
     * Not planned to be implemented for this platform.
     */
    override fun getScreenTimes(): List<ScreenTime>? = null

    init {
        Logger.i("init")
    }
}

@Composable
actual fun Graph(data: Collection<Number>) {
    Logger.e("Not yet implemented")
}
