package com.spacex.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Rocket
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.spacex.model.FalconInfo
import com.spacex.ui.FalconScreen
import com.spacex.ui.FalconsDetailScreen
import com.spacex.ui.HomeDetailScreen
import com.spacex.ui.MapScreen
import com.spacex.ui.SettingDetailScreen
import com.spacex.ui.SettingScreen
import com.spacex.viewmodel.SettingsViewModel
import kotlinx.serialization.json.Json


object Graph {
    const val NAVIGATION_BAR_SCREEN_GRAPH = "navigationBarScreenGraph"
}

sealed class Routes(var route: String) {
    data object Falcons : Routes("falcons")
    data object FalconsDetail : Routes("falconsDetail")
    data object Home : Routes("home")
    data object Setting : Routes("setting")
    data object HomeDetail : Routes("homeDetail")
    data object SettingDetail : Routes("settingDetail")
}

val navigationItemsLists = listOf(
    NavigationItem(
        unSelectedIcon = Icons.Outlined.Rocket,
        selectedIcon = Icons.Filled.Rocket,
        title = "Falcons",
        route = Routes.Falcons.route,
    ),
    NavigationItem(
        unSelectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        title = "Home",
        route = Routes.Home.route,
    ),
    NavigationItem(
        unSelectedIcon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        title = "Setting",
        route = Routes.Setting.route,
    ),
)




@Composable
fun BottomNavigationBar(
    items: List<NavigationItem>,
    currentRoute: String?,
    onItemClick: (NavigationItem) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items.forEach { navigationItem ->
            NavigationBarItem(
                selected = currentRoute == navigationItem.route,
                onClick = { onItemClick(navigationItem) },
                icon = {
                    Icon(
                        imageVector = if (navigationItem.route == currentRoute) navigationItem.selectedIcon else navigationItem.unSelectedIcon,
                        contentDescription = navigationItem.title,
                    )
                },
                label = {
                    Text(
                        text = navigationItem.title,
                        style = if (navigationItem.route == currentRoute) MaterialTheme.typography.labelLarge
                        else MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        }
    }
}

@Composable
fun NavigationSideBar(
    items: List<NavigationItem>,
    currentRoute: String?,
    onItemClick: (NavigationItem) -> Unit
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        items.forEach { navigationItem ->
            NavigationRailItem(
                selected = navigationItem.route == currentRoute,
                onClick = { onItemClick(navigationItem) },
                icon = {
                    Icon(
                        imageVector = if (navigationItem.route == currentRoute) navigationItem.selectedIcon else navigationItem.unSelectedIcon,
                        contentDescription = navigationItem.title,
                    )
                },
                modifier = Modifier.padding(vertical = 12.dp),
                label = {
                    Text(
                        text = navigationItem.title,
                        style = if (navigationItem.route == currentRoute) MaterialTheme.typography.labelLarge
                        else MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        }
    }
}


@Composable
fun RootNavGraph(
    rootNavController: NavHostController,
    innerPadding: PaddingValues,
    settingViewModel: SettingsViewModel
) {
    NavHost(
        navController = rootNavController,
        startDestination = Graph.NAVIGATION_BAR_SCREEN_GRAPH,
    ) {
        mainNavGraph(rootNavController = rootNavController, innerPadding = innerPadding, settingViewModel = settingViewModel)
        composable(
            route = Routes.HomeDetail.route,
        ) {
            rootNavController.previousBackStackEntry?.savedStateHandle?.get<String>("name")?.let { name ->
                HomeDetailScreen(rootNavController = rootNavController, name = name)
            }
        }
        composable(
            route = Routes.SettingDetail.route,
        ) {
            SettingDetailScreen(rootNavController = rootNavController)
        }
        composable(
            route = Routes.FalconsDetail.route,
        ) {
            rootNavController.previousBackStackEntry?.savedStateHandle?.get<String>("name")?.let {
                    jsonFalconInfo ->
                val falconInfo = Json.decodeFromString<FalconInfo>(jsonFalconInfo)
                FalconsDetailScreen(rootNavController = rootNavController,
                    falconInfo = falconInfo,
                    paddingValues = innerPadding)
            }
        }
    }
}

fun NavGraphBuilder.mainNavGraph(
    rootNavController: NavHostController,
    innerPadding: PaddingValues,
    settingViewModel: SettingsViewModel
) {
    navigation(
        startDestination = Routes.Home.route,
        route = Graph.NAVIGATION_BAR_SCREEN_GRAPH
    ) {
        composable(route = Routes.Falcons.route) {
            FalconScreen(rootNavController = rootNavController, paddingValues = innerPadding)
        }
        composable(route = Routes.Home.route) {
            MapScreen(paddingValues = innerPadding)
            //HomeScreen(rootNavController = rootNavController, paddingValues = innerPadding)
        }
        composable(route = Routes.Setting.route) {
            SettingScreen(
                rootNavController = rootNavController,
                paddingValues = innerPadding,
                viewModel = settingViewModel
            )
        }
    }
}
