package com.example.madlevel4task2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.madlevel4task2.ui.screens.MovieDetailScreen
import com.example.madlevel4task2.ui.screens.MovieOverviewScreen
import com.example.madlevel4task2.ui.screens.MoviesScreens
import com.example.madlevel4task2.ui.theme.MadLevel4Task2Theme
import com.example.madlevel4task2.viewmodel.MoviesViewModel

class MainActivity : ComponentActivity() {
    private val moviesViewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadLevel4Task2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    MovieNavHost(
                        navController = navController,
                        moviesViewModel = moviesViewModel,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieNavHost(
    navController: NavHostController,
    moviesViewModel: MoviesViewModel,
    modifier: Modifier
) {
    val selectedMovie by moviesViewModel.selectedMovie.observeAsState()
    NavHost(
        navController = navController,
        startDestination = MoviesScreens.MovieOverviewScreen.name,
        modifier = modifier
    ) {
        composable(MoviesScreens.MovieOverviewScreen.name) {
            MovieOverviewScreen(moviesViewModel, navController)
        }
        composable(MoviesScreens.MovieDetailScreen.name) {
            selectedMovie?.let {
                MovieDetailScreen(moviesViewModel)
            }
        }
    }
}