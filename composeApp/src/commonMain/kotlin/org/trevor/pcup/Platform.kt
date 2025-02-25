package org.trevor.pcup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import kotlin.time.Duration

/**
 * A common interface that specifies platform-specific functionality.
 *
 * Each platform implements their own [Platform] class.
 *
 * DO NOT use an `init` block to run code on platform init. Implement the [init] function instead.
 *
 * @property name Name of the implementing platform.
 * @property battery Remaining battery charge from 0-100 or `null` if not implemented or unknown.
 */
interface Platform {
    val name: String
    val battery: Int?

    /**
     * @return The platform's screen time information
     */
    fun getScreenTimeData(): List<ScreenTime>?

    /**
     * Send a notification.
     *
     * @param title The notification title.
     * @param message The notification message.
     */
    @Composable
    fun sendNotification(title: String, message: String)

    /**
     * @return The formatted battery level as a [String].
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
 * Each platform defines the `actual` function independently.
 *
 * @return The [Platform] implementation provided.
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
