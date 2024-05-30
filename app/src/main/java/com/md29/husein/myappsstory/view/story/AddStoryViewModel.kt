package com.md29.husein.myappsstory.view.story

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.md29.husein.myappsstory.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun upStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        latLng: LatLng? = null
    ) = storyRepository.upStory(photo, description, latLng = latLng)

}