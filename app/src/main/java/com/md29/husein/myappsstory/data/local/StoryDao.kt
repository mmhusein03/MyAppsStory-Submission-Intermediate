package com.md29.husein.myappsstory.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.md29.husein.myappsstory.data.networking.ListStories

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStories>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStories>

    @Query("SELECT * FROM story")
    fun getAllStories(): List<ListStories>

    @Query("SELECT photoUrl FROM story")
    fun getAllPhotoUrls(): List<String>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}