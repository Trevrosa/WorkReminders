package org.trevor.pcup

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

    fun batteryString(): String {
        return this.battery?.toString() ?: "battery level unimplemented"
    }
}

/**
 * Each platform will define the `actual` function independently.
 */
expect fun getPlatform(): Platform