package com.praxis.app.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Dashboard : Screen("dashboard")
    data object Goals : Screen("goals")
    data object GoalTree : Screen("goal_tree")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
}
