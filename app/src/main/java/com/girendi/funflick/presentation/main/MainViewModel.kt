package com.girendi.funflick.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girendi.funflick.R
import com.girendi.funflick.core.data.UiState
import com.girendi.funflick.core.domain.model.Genre
import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.domain.usecase.FetchMainUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val fetchMainUseCase: FetchMainUseCase
): ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> = _genres

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private var currentPage = 1

    init {
        fetchGenreList()
        fetchMovieList()
    }

    private fun fetchGenreList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = fetchMainUseCase.fetchListGenre()
            result.onSuccess { genres ->
                _genres.value = genres
                _uiState.value = UiState.Success
            }.onFailure { error ->
                _uiState.value = error.message?.let { UiState.Error(it) }
            }
        }
    }

    private fun fetchMovieList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = fetchMainUseCase.fetchListMovie(currentPage)
            result.onSuccess { movies ->
                _movies.value = movies
                _uiState.value = UiState.Success
            }.onFailure { error ->
                _uiState.value = error.message?.let { UiState.Error(it) }
            }
        }
    }

    fun getBackdropPathImage(path: String): String = "https://image.tmdb.org/t/p/w500${path}"

    fun getIcon(id: Int): Any {
        return when (id) {
            28 -> { R.drawable.ic_action }
            12 -> { R.drawable.ic_adventure }
            16 -> { R.drawable.ic_animation }
            35 -> { R.drawable.ic_comedy }
            80 -> { R.drawable.ic_crime }
            99 -> { R.drawable.ic_documentary }
            18 -> { R.drawable.ic_drama }
            10751 -> { R.drawable.ic_family }
            14 -> { R.drawable.ic_fantasy }
            36 -> { R.drawable.ic_history }
            27 -> { R.drawable.ic_horror }
            10402 -> { R.drawable.ic_music }
            9648 -> { R.drawable.ic_sci_fi }
            10749 -> { R.drawable.ic_romance }
            878 -> { R.drawable.ic_science_fiction }
            10770 -> { R.drawable.ic_movie_projector }
            53 -> { R.drawable.ic_john_wick }
            10752 -> { R.drawable.ic_war }
            37 -> { R.drawable.ic_western }

            else -> {
                R.drawable.ic_movie
            }
        }
    }
}