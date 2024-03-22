package com.girendi.funflick.core.domain.repository

import com.girendi.funflick.core.domain.model.Review

interface ReviewRepository {
    suspend fun fetchReviewList(movieId: Int, page:Int): Result<List<Review>>
}