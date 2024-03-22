package com.girendi.funflick.core.data.response

import com.girendi.funflick.core.domain.model.Review
import com.google.gson.annotations.SerializedName

data class ListReviewResponse(
    @field:SerializedName("results")
    val reviews: List<Review>
)