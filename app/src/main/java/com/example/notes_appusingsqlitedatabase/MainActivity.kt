package com.example.notes_appusingsqlitedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppNavigation()
        }
    }
}

@Composable
fun NotesAppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "notes") {
        composable("notes") {
            NotesApp(
                context = context,
                addCreateNote = {
                    navController.navigate("screenNotes")
                },
                onNoteClick = { note ->
                    navController.navigate("screenNotes/${note.id}/${note.title}/${note.content}")
                }
            )
        }
        composable(
            route = "screenNotes/{id}/{title}/{content}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""
            ScreenNotes(
                backScreenHome = { navController.popBackStack() },
                context = context,
                note = if (id == -1) null else Notes(id, title, content)
            )
        }
        composable("screenNotes") {
            ScreenNotes(
                backScreenHome = { navController.popBackStack() },
                context = context
            )
        }
    }
}