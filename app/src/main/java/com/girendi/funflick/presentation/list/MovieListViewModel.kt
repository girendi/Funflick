package com.girendi.funflick.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girendi.funflick.core.data.UiState
import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.domain.usecase.FetchMovieListUseCase
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val fetchMovieListUseCase: FetchMovieListUseCase
): ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private var currentPage = 1
    private var isLastPage = false

    fun fetchMovieByGenre(genreId: String) {
        if (isLastPage) {
            _uiState.postValue(UiState.Success)
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = fetchMovieListUseCase.fetchMovieByGenre(currentPage, genreId)
            result.onSuccess { movies ->
                _movies.value = _movies.value.orEmpty() + movies
                currentPage++
                isLastPage = movies.isEmpty()
                _uiState.value = UiState.Success
            }.onFailure { error ->
                _uiState.value = error.message?.let { UiState.Error(it) }
            }
        }
    }

    fun fetchMovieList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = fetchMovieListUseCase.fetchListMovie(currentPage)
            result.onSuccess { movies ->
                _movies.value = _movies.value.orEmpty() + movies
                currentPage++
                isLastPage = movies.isEmpty()
                _uiState.value = UiState.Success
            }.onFailure { error ->
                _uiState.value = error.message?.let { UiState.Error(it) }
            }
        }
    }

    fun getBackdropPathImage(path: String): String = "https://image.tmdb.org/t/p/w500${path}"
}