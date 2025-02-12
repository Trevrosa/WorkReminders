package org.trevor.pcup

import androidx.compose.runtime.Composable

/**
 * Allows platform-specific functionality.
 *
 * Each platform will have to implement a `Platform` class.
 *
 * @property name Name of the implementing platform.
 */
interface Platform {
    val name: String
    val battery: Float?

    fun batteryString(): String = this.battery?.toString() ?: "battery level null"
}

/**
 * Each platform will define the `actual` function independently.
 */
@Composable
expect fun getPlatform(): Platform
