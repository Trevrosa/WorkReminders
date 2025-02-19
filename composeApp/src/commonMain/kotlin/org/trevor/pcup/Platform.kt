package org.trevor.pcup

import androidx.compose.runtime.Composable
import kotlin.time.Duration

// TODO: rethink this (do we need an interface?)
/**
 * A common interface that specifies platform-specific functionality.
 *
 * Each platform will have to implement their own [Platform] class.
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

/**
 * Draw a graph using a [Collection] of [Number]s
 *
 * Does not worry about centering or layout.
 *
 * @param data The series to graph.
 */
@Composable
expect fun Graph(data: Collection<Number>)
