package com.spacex.model

import kotlinx.serialization.Serializable

@Serializable
data class FalconInfo(
    val id: String,
    val name: String,
    val dateUtc: String? = null,
    val links: String? = null,
    val pathSmall: String? = null,
    val pathLarge: String? = null,
    val description: String? = null,
    val details: String? = null,

    var isBookMark: Boolean = false
)
