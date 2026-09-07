package com.practicum.playlistmaker.media_library.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.media_library.favorites.FavoriteTracksFragment
import com.practicum.playlistmaker.media_library.playlists.PlaylistsFragment

class MediaLibraryPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FAVORITE_TRACKS_POSITION ->
                FavoriteTracksFragment.newInstance()

            else ->
                PlaylistsFragment.newInstance()
        }
    }

    companion object {
        const val FAVORITE_TRACKS_POSITION = 0
        private const val PAGE_COUNT = 2
    }
}