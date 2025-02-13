package org.trevor.pcup

import androidx.compose.runtime.Composable
import kotlin.time.Duration

/**
 * Allows platform-specific functionality.
 *
 * Each platform will have to implement a `Platform` class.
 *
 * @property name Name of the implementing platform.
 * @property battery Remaining battery charge from 0-100 or null if not implemented or unknown.
 */
interface Platform {
    val name: String
    val battery: Int?

    /**
     * Get the platform's screen time information
     */
    fun getScreenTimes(): List<ScreenTime>?
    fun batteryString(): String = this.battery?.let { "$it%" } ?: "no battery level"
}

class ScreenTime(val appName: String, val usage: Duration)

/**
 * Each platform will define the `actual` function independently.
 */
@Composable
expect fun getPlatform(): Platform
