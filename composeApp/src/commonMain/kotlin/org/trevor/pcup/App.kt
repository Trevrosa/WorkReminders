package org.trevor.pcup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.trevor.pcup.backend.validateSession
import org.trevor.pcup.screens.HomeTab
import org.trevor.pcup.screens.LimitsTab
import org.trevor.pcup.screens.Login
import org.trevor.pcup.screens.SettingsTab
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.skribi

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

val SESSION_ID_KEY = stringPreferencesKey("session_id")

suspend fun checkSessionValid(httpClient: HttpClient, dataStore: DataStore<Preferences>): Boolean {
    val sessionIdFlow =
        dataStore.data.map { prefs ->
            prefs[SESSION_ID_KEY]
        }

    val sessionId = sessionIdFlow.firstOrNull() ?: return false
    return validateSession(httpClient, sessionId)
}

lateinit var DataStore: DataStore<Preferences>

fun Platform.getDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(produceFile = {
        this.dataStorePath().toPath()
    })
}

@Composable
fun MyTabNavigator(httpClient: HttpClient, platform: Platform) {
    return TabNavigator(HomeTab) {
        Scaffold(
            content = {
                CurrentTab()
            },
            bottomBar = {
                BottomNavigation(Modifier.fillMaxWidth().height((24 + 16).dp)) {
                    TabNavigationItem(HomeTab)
                    TabNavigationItem(LimitsTab)
                    TabNavigationItem(SettingsTab(httpClient, platform))
                }
            }
        )
    }
}

@Composable
fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                painter = tab.options.icon!!,
                contentDescription = tab.options.title,
                tint = Color.White
            )
        }
    )
}

@Composable
fun AppEntry(httpClient: HttpClient, platform: Platform) {
    MaterialTheme {
        MyTabNavigator(httpClient, platform)
    }
}

@Preview
@Composable
fun AppInner() {
    // Do platform-specific work.
    val platform = getPlatform();
    val httpClient = remember { HttpClient() }

    var loading by remember { mutableStateOf("loading!") }

    LaunchedEffect(Unit) {
        // set once, then only use the set value
        DataStore = platform.getDataStore()
        loading = "getting datastore"
    }

    var sessionValid: Boolean? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        loading = "checking session"
        sessionValid = checkSessionValid(httpClient, DataStore)
    }

    if (sessionValid == null) {
        // the launched effect hasn't run yet.
        CenteringColumn {
            Text(loading, fontSize = 5.em)
        }
    } else if (sessionValid as Boolean) {
        AppEntry(httpClient, platform)
    } else {
        // TODO: make the error messages more pretty
        if (Login(httpClient)) {
            AppEntry(httpClient, platform)
        }
    }
}
