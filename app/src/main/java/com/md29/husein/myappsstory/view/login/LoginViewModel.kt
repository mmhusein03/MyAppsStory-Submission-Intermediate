package com.md29.husein.myappsstory.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.md29.husein.myappsstory.data.StoryRepository
import com.md29.husein.myappsstory.data.pref.UserModel
import com.md29.husein.myappsstory.data.pref.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun loginAcc(email: String, password: String) = storyRepository.login(email, password)
}