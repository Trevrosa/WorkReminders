package org.trevor.pcup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp

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
 * Column with [Alignment.Center] alignment and [Arrangement.Center] arrangement.
 */
@Composable
fun CenteringColumn(
    arrangement: Arrangement.Vertical = Arrangement.Center,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = arrangement,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

@Composable
fun Modifier.rounded(amount: Dp): Modifier = this.clip(RoundedCornerShape(amount))

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
