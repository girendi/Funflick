package com.girendi.funflick.core.data.repository

import com.girendi.funflick.core.data.api.ApiService
import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val apiService: ApiService
): MovieRepository {
    override suspend fun fetchListMovie(page: Int): Result<List<Movie>> =
        try {
            val response = apiService.getMovieList(page)
            Result.success(response.movies)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun fetchListMovieByGenre(page: Int, genreId: String): Result<List<Movie>> =
        try {
            val response = apiService.fetchMoviesByGenre(page, genreId)
            Result.success(response.movies)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun fetchMovieDetail(movieId: Int): Result<Movie> =
        try {
            val response = apiService.fetchMovieDetail(movieId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
}