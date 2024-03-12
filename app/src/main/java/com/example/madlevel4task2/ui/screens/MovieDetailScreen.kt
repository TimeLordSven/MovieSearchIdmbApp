package com.example.madlevel4task2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madlevel4task2.data.model.Movie
import com.example.madlevel4task2.viewmodel.MoviesViewModel

@Composable
fun MovieDetailScreen(moviesViewModel: MoviesViewModel) {
    val selectedMovie by moviesViewModel.selectedMovie.observeAsState()

    selectedMovie?.let { movie ->
        MovieDetailsContent(movie)
    }
}

@Composable
private fun MovieDetailsContent(movie: Movie) {
    val baseUrl = "https://image.tmdb.org/t/p/w500"
    val fullPosterUrl = movie.poster_path?.let { baseUrl + it }
    val fullBackdropUrl = movie.backdrop_path?.let { baseUrl + it }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            AsyncImage(
                model = fullBackdropUrl,
                contentDescription = "A funny image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = fullPosterUrl,
                    contentDescription = "A funny image"
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(4.dp)
                            .clip(MaterialTheme.shapes.small)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star Icon",
                            modifier = Modifier
                                .padding(4.dp)
                                .size(20.dp)
                        )
                        Text(
                            text = movie.vote_average.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        item {
            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}