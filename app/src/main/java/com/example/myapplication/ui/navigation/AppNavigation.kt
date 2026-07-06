package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.add_edit.AddEditNoteRoute
import com.example.myapplication.ui.home.HomeRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeRoute(
                onNavigateToAddNote = { navController.navigate("add_edit") },
                onNavigateToEditNote = { noteId ->
                    navController.navigate("add_edit?noteId=$noteId")
                },
            )
        }

        composable(
            route = "add_edit?noteId={noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->

            val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1
            AddEditNoteRoute(
                noteId = noteId,
                onBack = {
                    navController.popBackStack()
                }
            )

        }
    }
}