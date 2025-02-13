package org.trevor.pcup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Row with [Alignment.Center] alignment and [Arrangement.Center] arrangement.
 */
@Composable
fun CenteringRow(content: @Composable() (RowScope.() -> Unit)) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        content = content
    )
}

/**
 * Allows a nullable [Modifier] to be chained to a non-nullable [Modifier].
 *
 * If [other] is `null`, return [this], else, chain [this] with [other].
 */
@Composable
fun Modifier.then(other: Modifier?): Modifier {
    return if (other == null) {
        this
    } else {
        this.then(other)
    }
}
