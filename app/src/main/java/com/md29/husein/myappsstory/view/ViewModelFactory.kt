package com.md29.husein.myappsstory.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.md29.husein.myappsstory.data.pref.UserPreference
import com.md29.husein.myappsstory.di.Injection
import com.md29.husein.myappsstory.view.login.LoginViewModel
import com.md29.husein.myappsstory.view.main.MainViewModel
import com.md29.husein.myappsstory.view.maps.MapsViewModel
import com.md29.husein.myappsstory.view.signup.SignupViewModel
import com.md29.husein.myappsstory.view.story.AddStoryViewModel

class ViewModelFactory(
    private val pref: UserPreference,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref, Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref, Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(Injection.provideRepository(context)) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}