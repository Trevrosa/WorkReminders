package org.trevor.pcup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.home
import workreminders.composeapp.generated.resources.settings
import workreminders.composeapp.generated.resources.skribi
import workreminders.composeapp.generated.resources.today

/**
 * A function that returns a [Unit]
 */
typealias Fun = () -> Unit

@Composable
@Preview
fun NavBar(go1: Fun, go2: Fun, go3: Fun) {
    /**
     * Modifier for each button.
     */
    @Composable
    fun RowScope.NavBarButton(onClick: Fun) =
        Modifier
            .clickable(onClick = onClick)
            .background(MaterialTheme.colors.primary)
            // make each row item equal size, filling the whole width.
            .fillMaxSize()
            .weight(1F)

    /**
     * Create an [Image] with NavBarItem as its starting modifiers.
     */
    @Composable
    fun NavBarImage(vector: ImageVector, description: String?, modifier: Modifier? = null) {
        if (modifier == null) {
            Image(vector, description)
        } else {
            Image(vector, description, modifier)
        }
    }

    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier
                // icon height is 24 dp
                .fillMaxWidth().height((24 + 12).dp)
                .align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom,
        ) {
            Box(NavBarButton(go1), contentAlignment = Alignment.BottomCenter) {
                NavBarImage(vectorResource(Res.drawable.home), "home icon")
            }
            Box(NavBarButton(go2), contentAlignment = Alignment.BottomCenter) {
                NavBarImage(vectorResource(Res.drawable.today), "calendar icon")
            }
            Box(NavBarButton(go3), contentAlignment = Alignment.BottomCenter) {
                NavBarImage(vectorResource(Res.drawable.settings), "settings icon")
            }
        }
    }
}

/**
 * The home screen of the app.
 */
@Composable
@Preview
// TODO: support dark mode
// TODO: create my own theme
fun App() {
    // do the init stuff (hopefully once)
    init()

    Box(
        Modifier
            // BOTTOM INSET PADDING COLOR !!!!
            .background(MaterialTheme.colors.primary)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        AppInner()
    }
}

@Composable
@Preview
@Suppress("unused")
fun Test() {
    CenteringRow {
        Image(imageResource(Res.drawable.skribi), "xdd")
    }
}

/**
 * To be multiplied by a device's full width.
 */
@Suppress("unused")
private enum class SlideSide {
    FromLeft,
    FromRight;

    /**
     * [FromLeft] is -1, [FromRight] is 1.
     *
     * @param i The [Int] to be converted.
     * @return The converted [SlideSide], or `null` if [i] is not -1 or 1.
     */
    fun fromInt(i: Int): SlideSide? =
        when (i) {
            -1 -> FromLeft
            1 -> FromRight
            else -> null
        }

    /**
     * [FromLeft] is -1, [FromRight] is 1.
     */
    fun toInt(): Int =
        when (this) {
            FromLeft -> -1
            FromRight -> 1
        }
}

@Composable
@Preview
private fun AppInner() {
    // Do platform-specific work.
    val platform = getPlatform()

    var home by remember { mutableStateOf(true) }
    var limits by remember { mutableStateOf(false) }
    var settings by remember { mutableStateOf(false) }

    var limitsSlideSide by remember { mutableStateOf(SlideSide.FromRight) }

    val go1 = {
        home = true; limits = false; settings = false; limitsSlideSide = SlideSide.FromRight
    }
    val go2 = {
        home = false; limits = true; settings = false
    }
    val go3 = {
        home = false; limits = false; settings = true; limitsSlideSide = SlideSide.FromLeft
    }

    MaterialTheme {
        // TODO: remove later
        val startBattery = remember { platform.batteryString() }
        Text(
            startBattery,
            modifier = Modifier.zIndex(999F).padding(1.dp, 0.dp, 0.dp, 0.dp)
                .background(Color.LightGray)
        )

        NavBar(go1, go2, go3)

        /**
         * Assumes [this] is the device's screen's full width.
         *
         * @return Half of [this] multiplied by [slideSide].
         * @param slideSide The side to slide from.
         */
        fun Int.getOffset(slideSide: SlideSide) = this * slideSide.toInt() / 2

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = home,
            enter = slideInHorizontally(initialOffsetX = { fw -> fw.getOffset(SlideSide.FromLeft) }),
        ) {
            Home()
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = limits,
            enter = slideInHorizontally(initialOffsetX = { fw -> fw.getOffset(limitsSlideSide) }),
        ) {
            Limits()
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = settings,
            enter = slideInHorizontally(initialOffsetX = { fw -> fw.getOffset(SlideSide.FromRight) }),
        ) {
            Settings(platform)
        }
    }
}
