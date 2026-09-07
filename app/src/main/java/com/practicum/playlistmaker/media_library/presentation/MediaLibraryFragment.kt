package com.practicum.playlistmaker.media_library.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaLibraryBinding

class MediaLibraryFragment : Fragment() {

    private var _binding: FragmentMediaLibraryBinding? = null

    private val binding: FragmentMediaLibraryBinding
        get() = _binding!!

    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaLibraryBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter =
            MediaLibraryPagerAdapter(this)
    }

    private fun setupTabLayout() {
        tabLayoutMediator = TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->

            tab.text = when (position) {
                MediaLibraryPagerAdapter.FAVORITE_TRACKS_POSITION ->
                    getString(R.string.favorite_tracks)

                else ->
                    getString(R.string.playlists)
            }
        }

        tabLayoutMediator?.attach()
    }

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        tabLayoutMediator = null

        binding.viewPager.adapter = null

        _binding = null

        super.onDestroyView()
    }
}