package com.girendi.funflick.core.domain.usecase

import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.domain.model.Review
import com.girendi.funflick.core.domain.model.Video
import com.girendi.funflick.core.domain.repository.MovieRepository
import com.girendi.funflick.core.domain.repository.ReviewRepository
import com.girendi.funflick.core.domain.repository.VideoRepository

class FetchMovieDetailUseCase(
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository,
    private val videoRepository: VideoRepository
) {
    suspend fun fetchMovieDetail(movieId: Int): Result<Movie> =
        movieRepository.fetchMovieDetail(movieId)

    suspend fun fetchReviewList(movieId: Int, page: Int): Result<List<Review>> =
        reviewRepository.fetchReviewList(movieId, page)

    suspend fun fetchVideoList(movieId: Int): Result<List<Video>> =
        videoRepository.fetchVideoList(movieId)
}