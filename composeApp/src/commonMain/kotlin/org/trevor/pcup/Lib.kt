package org.trevor.pcup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Allows a nullable [Modifier] to be chained to a non-nullable [Modifier].
 *
 * If [other] is null, return [this], else, chain [this] with [other].
 */
@Composable
fun Modifier.then(other: Modifier?): Modifier {
    return if (other == null) {
        this
    } else {
        this.then(other)
    }
}