package com.practicum.playlistmaker.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryPagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FAVORITE_TRACKS_POSITION ->
                FavoriteTracksFragment.newInstance()

            PLAYLISTS_POSITION ->
                PlaylistsFragment.newInstance()

            else ->
                throw IllegalArgumentException("Неизвестная позиция: $position")
        }
    }

    companion object {
        const val FAVORITE_TRACKS_POSITION = 0
        const val PLAYLISTS_POSITION = 1
        private const val PAGE_COUNT = 2
    }
}