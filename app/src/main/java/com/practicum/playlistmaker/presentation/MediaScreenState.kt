package com.practicum.playlistmaker.presentation

data class MediaScreenState(
    val trackName: String,
    val artistName: String,
    val artworkUrl: String,
    val duration: String,
    val albumName: String,
    val year: String,
    val genre: String,
    val country: String,
    val progress: String,
    val isPlaying: Boolean,
    val isPlayButtonEnabled: Boolean
)
