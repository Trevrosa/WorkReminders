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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Allow using [ComposeImage] to create an [Image]
 */
@Suppress("NAME_SHADOWING")
@Composable
fun Image(
    image: ComposeImage,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    // ComposeImage::unwrap() must be ImageVector OR ImageBitmap
    val image = image.unwrap()
    (image as? ImageVector)?.let { imageVector ->
        androidx.compose.foundation.Image(
            imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter
        )
    } ?: run {
        androidx.compose.foundation.Image(
            image as ImageBitmap,
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter
        )
    }
}

/**
 * Representation of either [A] or [B].
 */
@OptIn(ExperimentalContracts::class)
sealed class Either<out A, out B> {
    data class Left<A>(val value: A) : Either<A, Nothing>()
    data class Right<B>(val value: B) : Either<Nothing, B>()

    /**
     * Return [A] if `this` is an instance of [Left], or `null` if `this` is an instance of [Right].
     */
    fun left(): A? {
        contract {
            returns(null) implies (this@Either is Right<B>)
        }

        return when (this) {
            is Left -> value
            is Right -> null
        }
    }

    /**
     * Return [B] if `this` is an instance of [Right], or `null` if `this` is an instance of [Left].
     */
    fun right(): B? {
        contract {
            returns(null) implies (this@Either is Left<A>)
        }

        return when (this) {
            is Left -> null
            is Right -> value
        }
    }

    /**
     * Get this [Either]'s [left]. If [left] is null, return [right].
     *
     * Will never be null.
     *
     * @return Value of type [A] or [B].
     */
    fun unwrap(): Any = this.left() ?: this.right()!!
}

/**
 * An `Image` that can be used by [Composable] functions.
 *
 * [Either.Left] is [ImageVector], [Either.Right] is [ImageBitmap]
 */
typealias ComposeImage = Either<ImageVector, ImageBitmap>

/**
 * Row with [Alignment.Center] alignment and [Arrangement.Center] arrangement.
 */
@Composable
fun CenteringRow(content: @Composable (RowScope.() -> Unit)) {
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
    content: @Composable (ColumnScope.() -> Unit)
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
