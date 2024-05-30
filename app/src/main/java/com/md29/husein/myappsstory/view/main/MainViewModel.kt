package com.md29.husein.myappsstory.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.md29.husein.myappsstory.data.StoryRepository
import com.md29.husein.myappsstory.data.pref.UserModel
import com.md29.husein.myappsstory.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun getUser(): Flow<UserModel> {
        return pref.getUser()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getStories() = storyRepository.getStory()

}