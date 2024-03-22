package com.girendi.funflick.core.domain.repository

import com.girendi.funflick.core.domain.model.Video

interface VideoRepository {
    suspend fun fetchVideoList(movieId: Int): Result<List<Video>>
}