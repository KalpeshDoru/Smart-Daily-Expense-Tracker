package com.imkalpesh.expense.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.imkalpesh.expense.R
import com.imkalpesh.expense.presentation.screen.home.HomeScreen
import com.imkalpesh.expense.presentation.screen.income.IncomeScreen
import com.imkalpesh.expense.presentation.screen.settings.SettingsScreen
import com.imkalpesh.expense.presentation.screen.statics.StaticsScreen
import com.imkalpesh.expense.presentation.transaction.TransactionScreen
import com.imkalpesh.expense.ui.theme.BluePrimary

@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    var bottomBarVisibility by remember {
        mutableStateOf(true)

    }
    Scaffold(bottomBar = {
        AnimatedVisibility(visible = bottomBarVisibility) {
            NavigationBottomBar(
                navController = navController,
                items = listOf(
                    NavItem(route = "/home", icon = R.drawable.ic_home),
                    NavItem(route = "/stats", icon = R.drawable.ic_stats),
                    NavItem(route = "/setting", icon = R.drawable.ic_setting),
                )
            )
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = "/home",
            modifier = Modifier.padding(it)
        ) {
            composable(route = "/home") {
                bottomBarVisibility = true
                HomeScreen(navController)
            }
            composable(route = "/add_income") {
                bottomBarVisibility = false
                IncomeScreen(navController, isIncome = true)
            }
            composable(route = "/add_exp") {
                bottomBarVisibility = false
                IncomeScreen(navController, isIncome = false)
            }
            composable(route = "/setting") {
                bottomBarVisibility = true
                SettingsScreen(navController)
            }
            composable(route = "/stats") {
                bottomBarVisibility = true
                StaticsScreen(navController)
            }
            composable(route = "/all_transactions") {
                bottomBarVisibility = true
                TransactionScreen(navController)
            }
        }
    }
}


data class NavItem(
    val route: String,
    val icon: Int
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items: List<NavItem>
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = null)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = BluePrimary,
                    selectedIconColor = BluePrimary,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}

