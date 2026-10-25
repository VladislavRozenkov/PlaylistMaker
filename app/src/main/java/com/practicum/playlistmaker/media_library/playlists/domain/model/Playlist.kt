package com.practicum.playlistmaker.media_library.playlists.domain.model

data class Playlist(
    val id: Long = 0L,
    val name: String,
    val description: String = "",
    val coverPath: String? = null,
    val trackIds: List<Long> = emptyList()
) {
    val trackCount: Int
        get() = trackIds.size
}
