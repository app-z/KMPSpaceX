package com.spacex.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.spacex.navigation.BottomNavigationBar
import com.spacex.navigation.NavigationItem
import com.spacex.navigation.NavigationSideBar
import com.spacex.navigation.RootNavGraph
import com.spacex.navigation.navigationItemsLists
import com.spacex.presentation.settings.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen(settingViewModel: SettingsViewModel) {
    val windowSizeClass = calculateWindowSizeClass()
    val isMediumExpandedWWSC by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        }
    }
    val rootNavController = rememberNavController()
    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }
    val navigationItem by remember {
        derivedStateOf {
            navigationItemsLists.find { it.route == currentRoute }
        }
    }
    val isMainScreenVisible by remember(isMediumExpandedWWSC) {
        derivedStateOf {
            navigationItem != null
        }
    }
    val isBottomBarVisible by remember(isMediumExpandedWWSC) {
        derivedStateOf {
            if (!isMediumExpandedWWSC) {
                navigationItem != null
            } else {
                false
            }
        }
    }
    MainScaffold(
        rootNavController = rootNavController,
        settingViewModel = settingViewModel,
        currentRoute = currentRoute,
        isMediumExpandedWWSC = isMediumExpandedWWSC,
        isBottomBarVisible = isBottomBarVisible,
        isMainScreenVisible = isMainScreenVisible,
        onItemClick = { currentNavigationItem ->
            rootNavController.navigate(currentNavigationItem.route) {
                popUpTo(rootNavController.graph.startDestinationRoute ?: "") {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@Composable
fun MainScaffold(
    rootNavController: NavHostController,
    settingViewModel: SettingsViewModel,
    currentRoute: String?,
    isMediumExpandedWWSC: Boolean,
    isBottomBarVisible: Boolean,
    isMainScreenVisible: Boolean,
    onItemClick: (NavigationItem) -> Unit,
) {
    Row {
        AnimatedVisibility(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            visible = isMediumExpandedWWSC && isMainScreenVisible,
            enter = slideInHorizontally(
                // Slide in from the left
                initialOffsetX = { fullWidth -> -fullWidth }
            ),
            exit = slideOutHorizontally(
                // Slide out to the right
                targetOffsetX = { fullWidth -> -fullWidth }
            )
        ) {
            NavigationSideBar(
                items = navigationItemsLists,
                currentRoute = currentRoute,
                onItemClick = { currentNavigationItem ->
                    onItemClick(currentNavigationItem)
                }
            )
        }

        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = isBottomBarVisible,
                    enter = slideInVertically(
                        // Slide in from the bottom
                        initialOffsetY = { fullHeight -> fullHeight }
                    ),
                    exit = slideOutVertically(
                        // Slide out to the bottom
                        targetOffsetY = { fullHeight -> fullHeight }
                    )
                ) {
                    BottomNavigationBar(
                        items = navigationItemsLists,
                        currentRoute = currentRoute,
                        onItemClick = { currentNavigationItem ->
                            onItemClick(currentNavigationItem)
                        }
                    )
                }
            }
        ) { innerPadding ->
            RootNavGraph(
                rootNavController = rootNavController,
                innerPadding = innerPadding,
                snackbarHostState = snackbarHostState,
                settingViewModel = settingViewModel
            )
        }
    }
}
