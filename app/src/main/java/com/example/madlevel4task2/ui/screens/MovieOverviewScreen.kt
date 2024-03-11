package com.example.madlevel4task2.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.madlevel4task2.R
import com.example.madlevel4task2.data.api.util.Resource
import com.example.madlevel4task2.data.model.Movie
import com.example.madlevel4task2.viewmodel.MoviesViewModel
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieOverviewScreen(moviesViewModel: MoviesViewModel) {
    val moviesState by moviesViewModel.movies.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Movie Search") },
                actions = {
                    SearchView { query ->
                        Log.d("SearchBar", "Search query: $query")
                        moviesViewModel.searchMovies(query)
                    }
                }
            )
        }
    ) {
        when (val resource = moviesState) {
            is Resource.Success<List<Movie>> -> {
                val movies = resource.data
                if (movies != null && movies.isNotEmpty()) {
                    // Log the list of movies
                    Log.d("MovieOverview", "List of Movies: $movies")
                } else {
                    Log.d("MovieOverview", "No movies found.")
                }
            }

            is Resource.Error<*> -> {
                Log.e("MovieOverview", "Error: ${resource.message}")
            }

            is Resource.Loading<*> -> {
                Log.d("MovieOverview", "Loading...")
            }

            else -> {
                Log.d("MovieOverview", "Unknown state")
            }
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView(
    searchTMDB: (String) -> Unit
) {
    val searchQueryState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(String()))
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = searchQueryState.value,
        onValueChange = { value ->
            searchQueryState.value = value
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = 18.sp),
        leadingIcon = {
            if (searchQueryState.value != TextFieldValue(String())) {
                IconButton(
                    onClick = {
                        searchQueryState.value =
                            TextFieldValue(String()) // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove search argument",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                // Use the MoviesViewModel to perform the search operation
                val query = searchQueryState.value.text
                searchTMDB(query)
                // Hide the keyboard
                keyboardController?.hide()
            }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search for movies in TMDB based on the search argument provided",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp),
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.search_movie_hint)
            )
        },
        singleLine = true,
        shape = RectangleShape, // The TextField has rounded corners top left and right by default
    )
}