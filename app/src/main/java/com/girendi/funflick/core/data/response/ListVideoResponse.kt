package com.girendi.funflick.core.data.response

import com.girendi.funflick.core.domain.model.Video
import com.google.gson.annotations.SerializedName

data class ListVideoResponse (
    @field:SerializedName("results")
    val videos: List<Video>
)