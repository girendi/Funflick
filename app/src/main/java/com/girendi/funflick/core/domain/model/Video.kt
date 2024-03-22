package com.girendi.funflick.core.domain.model

import com.google.gson.annotations.SerializedName

data class Video(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("key")
    val key: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("site")
    val site: String,
    @field:SerializedName("type")
    val type: String
)
