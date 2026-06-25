package com.practicum.playlistmaker.data.dto

data class ItunesResponseDto(
    val resultCount: Int,
    val results: List<TrackDto>
)
