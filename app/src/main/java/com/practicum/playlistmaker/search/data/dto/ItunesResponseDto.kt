package com.practicum.playlistmaker.search.data.dto

data class ItunesResponseDto(
    val resultCount: Int,
    val results: List<TrackDto>
)