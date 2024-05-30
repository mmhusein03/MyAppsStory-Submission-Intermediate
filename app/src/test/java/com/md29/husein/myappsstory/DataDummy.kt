package com.md29.husein.myappsstory

import com.md29.husein.myappsstory.data.networking.DataResponse
import com.md29.husein.myappsstory.data.networking.ListStories
import com.md29.husein.myappsstory.data.networking.LoginResult
import com.md29.husein.myappsstory.data.pref.UserModel

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStories> {
        val items: MutableList<ListStories> = arrayListOf()
        for (i in 0..100) {
            val storyList = ListStories(
                i.toString(),
                "user + $i",
                "photo + $i"
            )
            items.add(storyList)
        }
        return items
    }

    fun generateDummyUserModel(): UserModel {
        return UserModel(
            token = "userToken",
            userId = "userId",
            name = "userName",
            isLogin = true
        )
    }

    private fun generateDummyLoginResult(): LoginResult {
        return LoginResult(
            "userName",
            "userId",
            "tokenUser"
        )
    }

    fun generateDummyMapsEntity(): DataResponse {
        val storyList = arrayListOf<ListStories>()
        for (i in 0..10) {
            val postStory = ListStories(
                "story-$i",
                "nana",
                "https://story-api.dicoding.dev/images/stories/photos-1669830836207_3-8SiK_8.jpg",
                "2022-11-30T17:53:56.209Z",
                "adasdsadasd",
                5.27125,
                87.683845
            )

            storyList.add(postStory)
        }
        return DataResponse(
            false,
            "Stories fetched successfully",
            generateDummyLoginResult(),
            storyList
        )
    }

    fun generateDummyResponse(): DataResponse {
        return DataResponse(
            error = false,
            message = "Success",
            result = generateDummyLoginResult(),
            story = generateDummyStoryResponse()
        )
    }
}