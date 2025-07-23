package com.spacex.model

data class FalconInfo(
    val id: String,
    val name: String,
    val dateUtc: String? = null,
    val links: String? = null,
    val pathSmall: String? = null,
    val pathLarge: String? = null,
    val description: String? =null
)