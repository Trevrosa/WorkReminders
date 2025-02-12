package org.trevor.pcup

import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val battery: Float? = null

    init {
        Logger.i("init")
    }
}

// @Composable unused here, required because of expected function
@Composable
actual fun getPlatform(): Platform = JVMPlatform()
