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
}

/**
 * Each platform will define the `actual` function independently.
 */
expect fun getPlatform(): Platform