package com.girendi.funflick.core.data.response

import com.girendi.funflick.core.domain.model.Genre
import com.google.gson.annotations.SerializedName

data class ListGenresResponse(
    @field:SerializedName("genres")
    val genres: List<Genre>
)