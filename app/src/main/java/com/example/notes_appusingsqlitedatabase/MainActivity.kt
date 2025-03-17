package com.example.notes_appusingsqlitedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.notes_appusingsqlitedatabase.ui.theme.Notes_AppUsingSQLiteDatabaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           Notes_AppUsingSQLiteDatabaseTheme {
               MyApps()
            }
        }
    }

}


@Composable
fun MyApps(){
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = "home"){
        composable("home"){
            NotesApp(addCreateNote = {
                navController.navigate("addNotes")
            }, context = context)
        }
        composable("addNotes"){
            ScreenNotes(backScreenHome = {
                navController.navigate("home")
            },context)
        }
    }
}