package com.md29.husein.myappsstory.view.maps

import androidx.lifecycle.ViewModel
import com.md29.husein.myappsstory.data.StoryRepository

class MapsViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun getStoriesMaps() = storyRepository.getMaps()
}