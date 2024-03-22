package com.girendi.funflick.core.data.response

import com.girendi.funflick.core.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class ListMoviesResponse(
    @field:SerializedName("results")
    val movies: List<Movie>
)
