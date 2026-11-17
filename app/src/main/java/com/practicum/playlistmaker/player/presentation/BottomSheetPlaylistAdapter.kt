package com.practicum.playlistmaker.player.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemPlaylistBottomSheetBinding
import com.practicum.playlistmaker.media_library.playlists.domain.model.Playlist

class BottomSheetPlaylistAdapter :
    RecyclerView.Adapter<BottomSheetPlaylistViewHolder>() {

    private val playlists = mutableListOf<Playlist>()

    var onPlaylistClick: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetPlaylistViewHolder {

        val binding = ItemPlaylistBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BottomSheetPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BottomSheetPlaylistViewHolder,
        position: Int
    ) {
        val playlist = playlists[position]

        holder.bind(playlist)

        holder.itemView.setOnClickListener {
            onPlaylistClick?.invoke(playlist)
        }
    }

    override fun getItemCount(): Int = playlists.size

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylists(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}