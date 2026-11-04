package com.practicum.playlistmaker.media_library.playlists

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist
import java.io.File

class PlaylistViewHolder(
    private val binding: ItemPlaylistBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {

        binding.playlistName.text = playlist.name

        val count = playlist.trackCount

        val textResource = when {
            count % 100 in 11..14 -> R.string.playlist_tracks_many
            count % 10 == 1 -> R.string.playlist_tracks_one
            count % 10 in 2..4 -> R.string.playlist_tracks_few
            else -> R.string.playlist_tracks_many
        }

        binding.playlistTrackCount.text =
            binding.root.context.getString(textResource, count)

        val coverFile = playlist.coverPath?.let { File(it) }

        if (coverFile != null && coverFile.isFile) {

            binding.playlistPlaceholder.isVisible = false
            binding.playlistCover.isVisible = true

            Glide.with(binding.playlistCover)
                .load(coverFile)
                .centerCrop()
                .into(binding.playlistCover)

        } else {

            Glide.with(binding.playlistCover)
                .clear(binding.playlistCover)

            binding.playlistCover.isVisible = false
            binding.playlistPlaceholder.isVisible = true
        }
    }
}