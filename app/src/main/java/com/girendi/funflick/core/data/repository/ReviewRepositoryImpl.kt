package com.girendi.funflick.core.data.repository

import com.girendi.funflick.core.data.api.ApiService
import com.girendi.funflick.core.domain.model.Review
import com.girendi.funflick.core.domain.repository.ReviewRepository

class ReviewRepositoryImpl(
    private val apiService: ApiService
): ReviewRepository {
    override suspend fun fetchReviewList(movieId: Int, page:Int): Result<List<Review>> =
        try {
            val response = apiService.fetchMovieReviews(movieId, page)
            Result.success(response.reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
}