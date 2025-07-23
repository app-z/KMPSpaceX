package com.spacex.model


fun FalconEntity.mapToDomain() = FalconInfo(
    id = id,
    pathSmall = pathSmall,
    pathLarge = pathLarge,
    name = name,
    links = links,
    dateUtc = date_utc,
    details = details,
    isBookMark = isBookMark
)

fun RocketsResult.mapToEntity() = FalconEntity(
    id = id,
    date_utc = dateUtc,
    name = name,
    links = links?.article,
    pathSmall = links?.patch?.small,
    pathLarge = links?.patch?.large,
    details = details
)


fun RocketsResult.mapToDomain() = FalconInfo(
    id = id,
    dateUtc = dateUtc,
    name = name,
    links = links?.article,
    pathSmall = links?.patch?.small,
    pathLarge = links?.patch?.large,
    details = details
)
