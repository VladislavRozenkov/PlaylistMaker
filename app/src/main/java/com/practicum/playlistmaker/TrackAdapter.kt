package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.ItemTrackBinding

class TrackAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size

    class TrackViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.tvCount.text = track.trackTime

            Glide.with(binding.ivArt.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.snake)
                .error(R.drawable.snake)
                .transform(CenterCrop(), RoundedCorners(2))
                .into(binding.ivArt)
        }
    }
}