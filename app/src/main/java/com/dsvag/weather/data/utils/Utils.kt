package com.dsvag.weather.data.utils

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun convertEpochToOffsetDateTime(epochValue: Long, timeZone: Int): OffsetDateTime {
    return OffsetDateTime.of(
        LocalDateTime.ofEpochSecond(epochValue, 0, ZoneOffset.ofHours(timeZone)),
        ZoneOffset.UTC
    )
}

fun degreesToDirection(degree: Int): String {
    return when (degree) {
        in 0..11 -> "N"
        in 12..33 -> "NNE"
        in 34..56 -> "NE"
        in 57..78 -> "ENE"
        in 79..101 -> "E"
        in 102..123 -> "ESE"
        in 124..146 -> "SE"
        in 147..168 -> "SSE"
        in 169..191 -> "S"
        in 192..213 -> "SSW"
        in 214..236 -> "SW"
        in 237..258 -> "WSW"
        in 259..281 -> "W"
        in 282..303 -> "WNW"
        in 304..326 -> "NW"
        in 327..348 -> "NNW"
        in 349..360 -> "N"
        else -> error("impossible")
    }
}

val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val dateWithTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d , HH:mm")
val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("E, MMMM d")