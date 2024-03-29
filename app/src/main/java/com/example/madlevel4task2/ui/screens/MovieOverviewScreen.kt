package com.example.madlevel4task2.ui.screens
//Author: Sven Molenaar
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.madlevel4task2.R
import com.example.madlevel4task2.data.api.util.Resource
import com.example.madlevel4task2.data.model.Movie
import com.example.madlevel4task2.viewmodel.MoviesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieOverviewScreen(moviesViewModel: MoviesViewModel, navController: NavHostController) {
    val moviesState by moviesViewModel.movies.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    SearchView(
                        searchTMDB = { query ->
                            Log.d("SearchBar", "Search query: $query")
                            moviesViewModel.searchMovies(query)
                        },
                        moviesViewModel = moviesViewModel
                    )
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val resource = moviesState) {
                    is Resource.Success<List<Movie>> -> {
                        val movies = resource.data
                        if (!movies.isNullOrEmpty()) {
                            MovieGrid(movies, navController, moviesViewModel)
                        } else {
                            Text(
                                text = stringResource(R.string.no_movies_found),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    is Resource.Error<*> -> {
                        Text(
                            text = stringResource(R.string.search_error, resource.message as Any),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(4.dp)
                        )
                    }

                    is Resource.Loading<*> -> {
                        Text(
                            text = stringResource(R.string.still_loading),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(4.dp)
                        )
                        CircularProgressIndicator()
                    }

                    else -> {
                        Text(
                            text = stringResource(R.string.unknown_state),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SearchView(
    searchTMDB: (String) -> Unit,
    moviesViewModel: MoviesViewModel
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
                            TextFieldValue(String())
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
                val query = searchQueryState.value.text
                searchTMDB(query)
                moviesViewModel.setSelectedMovie(null)
                keyboardController?.hide()
            }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search for movies in IMDB based on the search argument provided",
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
        shape = RectangleShape,
    )
}

@Composable
private fun MovieGrid(
    movies: List<Movie>,
    navController: NavHostController,
    moviesViewModel: MoviesViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        items(movies) { movie ->
            MovieItem(
                movie = movie,
                navController = navController,
                moviesViewModel = moviesViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieItem(
    movie: Movie,
    navController: NavHostController,
    moviesViewModel: MoviesViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        onClick = {
            moviesViewModel.setSelectedMovie(movie)
            navController.navigate(MoviesScreens.MovieDetailScreen.name)
        }
    ) {
        Column {
            val baseUrl = "https://image.tmdb.org/t/p/w500"
            val fullImageUrl = movie.poster_path?.let { baseUrl + it }
            if (!movie.poster_path.isNullOrBlank()) {
                AsyncImage(
                    model = fullImageUrl,
                    contentDescription = "A funny image"
                )
            } else {
                AsyncImage(
                    model = "https://critics.io/img/movies/poster-placeholder.png",
                    contentDescription = "A funny image"
                )
            }
        }
    }
}