package com.example.madlevel4task2.viewmodel
//Author: Sven Molenaar
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel4task2.data.api.util.Resource
import com.example.madlevel4task2.data.model.Movie
import com.example.madlevel4task2.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository()

    private val _movies = MutableLiveData<Resource<List<Movie>>>()

    private val _selectedMovie = MutableLiveData<Movie?>()
    val movies: LiveData<Resource<List<Movie>>>
        get() = _movies

    val selectedMovie: LiveData<Movie?>
        get() = _selectedMovie

    fun searchMovies(query: String) {
        _movies.value = Resource.Loading()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = repository.searchMovies(query)

                withContext(Dispatchers.Main) {
                    _movies.value = result
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _movies.value = Resource.Error("An error occurred: ${e.message}")
                }
            }
        }
    }

    fun setSelectedMovie(movie: Movie?) {
        _selectedMovie.value = movie
    }
}