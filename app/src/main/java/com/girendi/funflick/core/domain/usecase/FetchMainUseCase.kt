package com.girendi.funflick.core.domain.usecase

import com.girendi.funflick.core.domain.model.Genre
import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.domain.repository.GenreRepository
import com.girendi.funflick.core.domain.repository.MovieRepository

class FetchMainUseCase(
    private val genreRepository: GenreRepository,
    private val movieRepository: MovieRepository
) {
    suspend fun fetchListGenre(): Result<List<Genre>> =
        genreRepository.fetchListGenres()

    suspend fun fetchListMovie(page: Int): Result<List<Movie>> =
        movieRepository.fetchListMovie(page)
}