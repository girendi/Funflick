package com.girendi.funflick.core.domain.usecase

import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.domain.repository.MovieRepository

class FetchMovieListUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun fetchMovieByGenre(page: Int, genreId: String): Result<List<Movie>> =
        movieRepository.fetchListMovieByGenre(page, genreId)

    suspend fun fetchListMovie(page: Int): Result<List<Movie>> =
        movieRepository.fetchListMovie(page)
}