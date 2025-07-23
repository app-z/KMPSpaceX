package com.spacex.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RocketsResult (

//    @SerialName("fairings"              ) var fairings           : Fairings?           = Fairings(),
    @SerialName("links"                 ) var links              : Links?              = Links(),
    @SerialName("static_fire_date_utc"  ) var staticFireDateUtc  : String?             = null,
    @SerialName("static_fire_date_unix" ) var staticFireDateUnix : Int?                = null,
    @SerialName("net"                   ) var net                : Boolean?            = null,
    @SerialName("window"                ) var window             : Int?                = null,
    @SerialName("rocket"                ) var rocket             : String             = "Unknown",
    @SerialName("success"               ) var success            : Boolean?            = null,
//    @SerialName("failures"              ) var failures           : ArrayList<Failures> = arrayListOf(),
    @SerialName("details"               ) var details            : String?             = null,
//    @SerialName("crew"                  ) var crew               : ArrayList<String>   = arrayListOf(),
//    @SerialName("ships"                 ) var ships              : ArrayList<String>   = arrayListOf(),
//    @SerialName("capsules"              ) var capsules           : ArrayList<String>   = arrayListOf(),
//    @SerialName("payloads"              ) var payloads           : ArrayList<String>   = arrayListOf(),
    @SerialName("launchpad"             ) var launchpad          : String?             = null,
    @SerialName("flight_number"         ) var flightNumber       : Int?                = null,
    @SerialName("name"                  ) var name               : String             = "Unknown",
    @SerialName("date_utc"              ) var dateUtc            : String?             = null,
    @SerialName("date_unix"             ) var dateUnix           : Int?                = null,
    @SerialName("date_local"            ) var dateLocal          : String?             = null,
    @SerialName("date_precision"        ) var datePrecision      : String?             = null,
    @SerialName("upcoming"              ) var upcoming           : Boolean?            = null,
//    @SerialName("cores"                 ) var cores              : ArrayList<Cores>    = arrayListOf(),
    @SerialName("auto_update"           ) var autoUpdate         : Boolean?            = null,
    @SerialName("tbd"                   ) var tbd                : Boolean?            = null,
    @SerialName("launch_library_id"     ) var launchLibraryId    : String?             = null,
    @SerialName("id"                    ) var id                 : String             = ""

)

@Serializable
data class Fairings (

    @SerialName("reused"           ) var reused          : Boolean?          = null,
    @SerialName("recovery_attempt" ) var recoveryAttempt : Boolean?          = null,
    @SerialName("recovered"        ) var recovered       : Boolean?          = null,
    @SerialName("ships"            ) var ships           : ArrayList<String> = arrayListOf()

)

@Serializable
data class Patch (
    @SerialName("small" ) var small : String? = null,
    @SerialName("large" ) var large : String? = null
)

@Serializable
data class Links (

    @SerialName("patch"      ) var patch     : Patch?  = Patch(),
//    @SerialName("reddit"     ) var reddit    : Reddit? = Reddit(),
//    @SerialName("flickr"     ) var flickr    : Flickr? = Flickr(),
    @SerialName("presskit"   ) var presskit  : String? = null,
    @SerialName("webcast"    ) var webcast   : String? = null,
    @SerialName("youtube_id" ) var youtubeId : String? = null,
    @SerialName("article"    ) var article   : String? = null,
    @SerialName("wikipedia"  ) var wikipedia : String? = null

)
