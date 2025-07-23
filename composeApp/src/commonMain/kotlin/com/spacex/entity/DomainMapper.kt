package com.spacex.entity

import com.spacex.model.FalconEntity
import com.spacex.model.RocketsResult
import com.spacex.model.FalconInfo


fun FalconEntity.mapToDomain() = FalconInfo(
    id = id,
    pathSmall = pathSmall,
    pathLarge = pathLarge,
    name = name,
    links = links,
    dateUtc = date_utc
)

fun RocketsResult.mapToEntity() = FalconEntity(
    id = id,
    date_utc = dateUtc,
    name = name,
    links = links?.article,
    pathSmall = links?.patch?.small,
    pathLarge = links?.patch?.large
)


fun RocketsResult.mapToDomain() = FalconInfo(
    id = id,
    dateUtc = dateUtc,
    name = name,
    links = links?.article,
    pathSmall = links?.patch?.small,
    pathLarge = links?.patch?.large
)
