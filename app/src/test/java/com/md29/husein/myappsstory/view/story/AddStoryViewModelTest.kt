package com.md29.husein.myappsstory.view.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.md29.husein.myappsstory.DataDummy
import com.md29.husein.myappsstory.data.Result
import com.md29.husein.myappsstory.data.StoryRepository
import com.md29.husein.myappsstory.data.networking.DataResponse
import com.md29.husein.myappsstory.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    private lateinit var addStoryViewModel: AddStoryViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepo: StoryRepository

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(mockRepo)
    }

    @Test
    fun `when post story is successful and should not null`() {
        val file = mock(File::class.java)
        val description = "walelaje".toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val latLng = LatLng(99.2, 22.9)
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        val expectedPostStory = MutableLiveData<Result<DataResponse>>()
        expectedPostStory.value = Result.Success(DataDummy.generateDummyResponse())
        Mockito.`when`(mockRepo.upStory(imageMultipart, description, latLng)).thenReturn(expectedPostStory)

        val actualPostStory =
            addStoryViewModel.upStory(imageMultipart, description, latLng).getOrAwaitValue()

        verify(mockRepo).upStory(imageMultipart, description, latLng)
        assertTrue(actualPostStory is Result.Success)
        assertNotNull(actualPostStory)
    }
}