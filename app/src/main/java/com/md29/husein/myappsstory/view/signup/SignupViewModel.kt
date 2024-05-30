package com.md29.husein.myappsstory.view.signup

import androidx.lifecycle.ViewModel
import com.md29.husein.myappsstory.data.StoryRepository

class SignupViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun signupAcc(
        name: String,
        email: String,
        password: String,
    ) = storyRepository.signupAcc(name, email, password)

}