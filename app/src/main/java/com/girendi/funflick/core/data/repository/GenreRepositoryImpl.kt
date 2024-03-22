package com.girendi.funflick.core.data.repository

import com.girendi.funflick.core.data.api.ApiService
import com.girendi.funflick.core.domain.model.Genre
import com.girendi.funflick.core.domain.repository.GenreRepository

class GenreRepositoryImpl (
    private val apiService: ApiService
): GenreRepository {
    override suspend fun fetchListGenres(): Result<List<Genre>> =
        try {
            val response = apiService.getMovieGenres()
            Result.success(response.genres)
        } catch (e: Exception) {
            Result.failure(e)
        }
}