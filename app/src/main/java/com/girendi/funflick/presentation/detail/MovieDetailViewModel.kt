package com.girendi.funflick.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girendi.funflick.core.data.UiState
import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.domain.model.Review
import com.girendi.funflick.core.domain.model.Video
import com.girendi.funflick.core.domain.usecase.FetchMovieDetailUseCase
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val fetchMovieDetailUseCase: FetchMovieDetailUseCase
): ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private val _video = MutableLiveData<Video>()
    val video: LiveData<Video> = _video

    private var currentPage = 1
    private var isLastPage = false

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = fetchMovieDetailUseCase.fetchMovieDetail(movieId)
            result.onSuccess { movie ->
                _movie.value = movie
                _uiState.value = UiState.Success
            }.onFailure {
                _uiState.value = it.message?.let { it1 -> UiState.Error(it1) }
            }
        }
    }

    fun fetchReviewList(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = fetchMovieDetailUseCase.fetchReviewList(movieId, currentPage)
            result.onSuccess { reviews ->
                _reviews.value = _reviews.value.orEmpty() + reviews
                currentPage++
                isLastPage = reviews.isEmpty()
                _uiState.value = UiState.Success
            }.onFailure {
                _uiState.value = it.message?.let { it1 -> UiState.Error(it1) }
            }
        }
    }

    fun fetchVideo(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = fetchMovieDetailUseCase.fetchVideoList(movieId)
            result.onSuccess { videos ->
                val trailers = videos.filter {
                    it.type == "Trailer"
                }
                if (trailers.isNotEmpty()) {
                    _video.value = trailers[0]
                }
                _uiState.value = UiState.Success
            }.onFailure {
                _uiState.value = it.message?.let { it1 -> UiState.Error(it1) }
            }
        }
    }

    fun getBackdropPathImage(path: String): String = "https://image.tmdb.org/t/p/w500${path}"
}