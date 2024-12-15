package com.example.blacklotus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blacklotus.ui.screens.mainscreen.MainScreen


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "mainscreen",
    ) {
        composable("mainscreen") { MainScreen() }
    }
}

