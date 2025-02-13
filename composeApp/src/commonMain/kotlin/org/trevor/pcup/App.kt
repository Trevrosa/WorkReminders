@file:Suppress("NAME_SHADOWING")

package org.trevor.pcup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
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
typealias FunUnit = () -> Unit

val NavBarHeight: Dp = 60.dp

@Composable
@Preview
fun NavBar(go1: FunUnit, go2: FunUnit, go3: FunUnit) {
    /**
     * Modifier for each button.
     */
    @Composable
    fun RowScope.NavBarButton(onClick: FunUnit) =
        Modifier
            .clickable(onClick = onClick)
            .background(MaterialTheme.colors.primary)
            // make each row item equal size, filling the whole width.
            .fillMaxSize()
            .weight(1F)

    /**
     * Modifier for each button's contents.
     */
    @Composable
    fun NavBarItem() = Modifier
        // rounded corner
        .clip(RoundedCornerShape(10))
        .background(Color.DarkGray)
        .padding(4.dp, 2.dp)

    /**
     * Create an [Image] with NavBarItem as its starting modifiers.
     */
    @Composable
    fun NavBarImage(vector: ImageVector, description: String?, modifier: Modifier? = null) {
        val modifier = NavBarItem().then(modifier)
        Image(vector, description, modifier)
    }

    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().height(NavBarHeight).align(Alignment.BottomStart)
                .background(MaterialTheme.colors.secondary),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(NavBarButton(go1), contentAlignment = Alignment.Center) {
                NavBarImage(vectorResource(Res.drawable.home), "home icon")
            }
            Box(NavBarButton(go2), contentAlignment = Alignment.Center) {
                NavBarImage(vectorResource(Res.drawable.today), "calendar icon")
            }
            Box(NavBarButton(go3), contentAlignment = Alignment.Center) {
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
fun App() {
    // TODO: change bottom (navigation) insets color, make navigation set at the bottom
    Box(Modifier.windowInsetsPadding(WindowInsets.statusBars.add(WindowInsets.navigationBars))) {
        AppInner()
    }
}

@Composable
@Preview
fun Test() {
    CenteringRow {
        Image(imageResource(Res.drawable.skribi), "xdd")
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

    val go1 = { home = true; limits = false; settings = false; }
    val go2 = { home = false; limits = true; settings = false; }
    val go3 = { home = false; limits = false; settings = true; }

    MaterialTheme {
        val startBattery = remember { platform.batteryString() }
        Text(startBattery, modifier = Modifier.zIndex(999F))

        NavBar(go1, go2, go3)

        if (home) {
            Home()
        }

        if (limits) {
            Limits()
        }

        if (settings) {
            Settings()
        }
    }
}
