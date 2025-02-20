package org.trevor.pcup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import kotlin.time.Duration

/**
 * A common interface that specifies platform-specific functionality.
 *
 * Each platform has to implement their own [Platform] class.
 *
 * DO NOT use an `init` block for code intended to run once. Implement the [init] function instead.
 *
 * @property name Name of the implementing platform.
 * @property battery Remaining battery charge from 0-100 or `null` if not implemented or unknown.
 */
interface Platform {
    val name: String
    val battery: Int?

    /**
     * Get the platform's screen time information
     */
    fun getScreenTimeData(): List<ScreenTime>?

    /**
     * Send a notification.
     *
     * @param message The notification message.
     */
    @Composable
    fun sendNotification(message: String)

    /**
     * Format the battery level as a [String].
     */
    fun batteryString(): String = this.battery?.let { "$it%" } ?: "no battery level"
}

/**
 * An app's screen time.
 *
 * @property appName Name of the app.
 * @property usage Total usage for this app.
 */
class ScreenTime(val appName: String, val usage: Duration)

/**
 * Each platform will define the `actual` function independently.
 */
@Composable
expect fun getPlatform(): Platform

/**
 * Do init stuff.
 */
@Composable
@NonRestartableComposable
expect fun init()

/**
 * Draw a graph using a [Collection] of [Number]s
 *
 * Does not worry about centering or layout.
 *
 * @param data The series to graph.
 */
@Composable
expect fun Graph(data: Collection<Number>)
