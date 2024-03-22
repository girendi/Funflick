package com.girendi.funflick.core.domain.repository

import com.girendi.funflick.core.domain.model.Genre

interface GenreRepository {
    suspend fun fetchListGenres(): Result<List<Genre>>
}