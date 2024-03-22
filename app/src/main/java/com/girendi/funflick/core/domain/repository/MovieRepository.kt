package com.girendi.funflick.core.domain.repository

import com.girendi.funflick.core.domain.model.Movie

interface MovieRepository {
    suspend fun fetchListMovie(page: Int): Result<List<Movie>>
    suspend fun fetchListMovieByGenre(page: Int, genreId: String): Result<List<Movie>>
    suspend fun fetchMovieDetail(movieId: Int): Result<Movie>
}