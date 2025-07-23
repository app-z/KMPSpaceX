package com.spacex.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SpaceX")
data class FalconEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date_utc") var date_utc: String? = null,
    @ColumnInfo(name = "links") var links: String? = null,
    @ColumnInfo(name = "pathSmall") var pathSmall: String? = null,
    @ColumnInfo(name = "pathLarge") var pathLarge: String? = null
//    @ColumnInfo(name = "links") var links: Links? = Links(),
)

// TODO()
//data class Links(
//    @ColumnInfo(name = "patch") var patch: Patch? = Patch(),
//)
//
//data class Patch (
//    @ColumnInfo(name = "small" ) var small : String? = null,
//    @ColumnInfo(name = "large" ) var large : String? = null
//)
