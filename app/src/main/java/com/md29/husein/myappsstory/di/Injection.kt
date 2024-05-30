package com.md29.husein.myappsstory.di

import android.content.Context
import com.md29.husein.myappsstory.data.StoryRepository
import com.md29.husein.myappsstory.data.local.StoryDatabase
import com.md29.husein.myappsstory.data.networking.ApiConfig
import com.md29.husein.myappsstory.data.pref.UserPreference
import com.md29.husein.myappsstory.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(apiService ,database)
    }
}