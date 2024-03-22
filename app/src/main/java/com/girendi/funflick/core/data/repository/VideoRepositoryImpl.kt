package com.girendi.funflick.core.data.repository

import com.girendi.funflick.core.data.api.ApiService
import com.girendi.funflick.core.domain.model.Video
import com.girendi.funflick.core.domain.repository.VideoRepository

class VideoRepositoryImpl(
    private val apiService: ApiService
): VideoRepository {
    override suspend fun fetchVideoList(movieId: Int): Result<List<Video>> =
        try {
            val result = apiService.fetchMovieVideos(movieId)
            Result.success(result.videos)
        } catch (e: Exception) {
            Result.failure(e)
        }
}