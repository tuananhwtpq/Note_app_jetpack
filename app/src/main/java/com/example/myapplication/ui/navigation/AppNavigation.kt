package com.example.myapplication.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.data.local.NoteDatabase
import com.example.myapplication.data.repository.NoteRepoImpl
import com.example.myapplication.ui.add_edit.AddEditNoteRoute
import com.example.myapplication.ui.add_edit.AddEditViewModel
import com.example.myapplication.ui.home.HomeRoute
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.utils.ViewModelFactory

@Composable
fun AppNavigation(context: Context) {
    val navController = rememberNavController()

    val db = NoteDatabase.getInstance(context)
    val repository = NoteRepoImpl(db.noteDao())

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {

            val factory = remember { ViewModelFactory(repository) }
            val homeViewModel: HomeViewModel = viewModel(
                factory = factory
            )

            HomeRoute(
                viewModel = homeViewModel,
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
            val factory = remember { ViewModelFactory(repository, noteId) }
            val addEditViewModel: AddEditViewModel = viewModel(factory = factory)

            AddEditNoteRoute(
                viewModel = addEditViewModel,
                noteId = noteId,
                onBack = {
                    navController.popBackStack()
                }
            )

        }
    }
}