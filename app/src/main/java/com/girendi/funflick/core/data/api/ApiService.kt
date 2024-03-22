package com.girendi.funflick.core.data.api

import com.girendi.funflick.core.data.response.ListGenresResponse
import com.girendi.funflick.core.data.response.ListMoviesResponse
import com.girendi.funflick.core.data.response.ListReviewResponse
import com.girendi.funflick.core.data.response.ListVideoResponse
import com.girendi.funflick.core.domain.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list")
    suspend fun getMovieGenres(): ListGenresResponse

    @GET("movie/now_playing")
    suspend fun getMovieList(
        @Query("page") page: Int,
    ): ListMoviesResponse

    @GET("discover/movie")
    suspend fun fetchMoviesByGenre(
        @Query("page") page: Int,
        @Query("with_genres") genreId: String
    ): ListMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun fetchMovieDetail(
        @Path("movie_id") movieId: Int
    ): Movie

    @GET("movie/{movie_id}/reviews")
    suspend fun fetchMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int
    ): ListReviewResponse

    @GET("movie/{movieId}/videos")
    suspend fun fetchMovieVideos(
        @Path("movieId") movieId: Int
    ): ListVideoResponse
}