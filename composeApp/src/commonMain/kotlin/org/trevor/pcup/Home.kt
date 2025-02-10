package org.trevor.pcup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.skribi

/**
 * A function that returns a [Unit]
 */
typealias FunUnit = () -> Unit

val NavBarHeight: Dp = 50.dp

@Composable
@Preview
fun NavBar(go1: FunUnit, go2: FunUnit, go3: FunUnit) {
    /**
     * Modifier for each button.
     */
    @Composable
    fun RowScope.navBarButton(onClick: FunUnit) =
        Modifier
            .clickable(onClick = onClick)
            .background(MaterialTheme.colors.primary)
            // rounded corner
            .clip(RoundedCornerShape(40.dp))
            // make each row item equal size, filling the whole width.
            .fillMaxSize()
            .weight(1F)

    @Composable
    fun navBarText(text: String, modifier: Modifier? = null) {
        val customModifier: Modifier = Modifier
            .background(Color.LightGray)
            .padding(5.dp)
            .clip(RoundedCornerShape(1.dp))
        if (modifier == null) {
            Text(text, modifier = customModifier)
        } else {
            Text(text, modifier = customModifier.then(modifier))
        }
    }

    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().height(NavBarHeight).align(Alignment.BottomStart)
                .background(MaterialTheme.colors.secondary),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(navBarButton(go1), contentAlignment = Alignment.Center) {
                navBarText("hi1")
            }
            Box(navBarButton(go2), contentAlignment = Alignment.Center) {
                navBarText("hi2")
            }
            Box(navBarButton(go3), contentAlignment = Alignment.Center) {
                navBarText("hi3")
            }
        }
    }
}

/**
 * The home screen of the app.
 */
@Composable
@Preview
fun Home() {
    // Do platform-specific work.
    val platform = getPlatform()

    var show by remember { mutableStateOf(false) }

    val go1 = {
        show = true
    }
    val go2 = {
        show = false
    }
    val go3 = {
        show = !show
    }
    BottomNavigation { }
    MaterialTheme {
        Row(horizontalArrangement = Arrangement.End) {
            Text(platform.batteryString())
        }

        NavBar(go1, go2, go3)

        if (show) {
            Image(imageResource(Res.drawable.skribi), "xdd")
        }
    }
}
