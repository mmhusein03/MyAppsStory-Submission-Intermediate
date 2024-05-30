package com.md29.husein.myappsstory.data.networking


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun signupAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): DataResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String,
    ): DataResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0,
    ): DataResponse

    @Multipart
    @POST("stories")
    suspend fun postImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float? = null,
        @Part("lon") lon: Float? = null,
    ): DataResponse

}