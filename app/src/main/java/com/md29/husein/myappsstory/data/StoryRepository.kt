package com.md29.husein.myappsstory.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.md29.husein.myappsstory.data.local.StoryDatabase
import com.md29.husein.myappsstory.data.networking.ApiService
import com.md29.husein.myappsstory.data.networking.DataResponse
import com.md29.husein.myappsstory.data.networking.ErrorResponse
import com.md29.husein.myappsstory.data.networking.ListStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase,
) {

    fun signupAcc(
        name: String,
        email: String,
        password: String,
    ): LiveData<Result<DataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.signupAccount(name, email, password)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        } catch (e: Exception) {
            Log.d("StoryRepository", "Signup: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(
        email: String,
        password: String,
    ): LiveData<Result<DataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.loginAccount(email, password)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        } catch (e: Exception) {
            Log.d("StoryRepository", "Login: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStory(): LiveData<PagingData<ListStories>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun upStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        latLng: LatLng?= null
    ): LiveData<Result<DataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val lat = latLng?.latitude?.toFloat()
            val lon = latLng?.longitude?.toFloat()
            val result = apiService.postImage(photo, description, lat = lat, lon = lon)
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        } catch (e: Exception) {
            Log.d("StoryRepository", "upStory: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getMaps(): LiveData<Result<DataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getStories(location = 1)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}