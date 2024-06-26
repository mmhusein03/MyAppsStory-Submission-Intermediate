package com.md29.husein.myappsstory.view.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.md29.husein.myappsstory.DataDummy
import com.md29.husein.myappsstory.MainDispatcherRule
import com.md29.husein.myappsstory.data.Result
import com.md29.husein.myappsstory.data.StoryRepository
import com.md29.husein.myappsstory.data.networking.DataResponse
import com.md29.husein.myappsstory.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyMaps = DataDummy.generateDummyMapsEntity()
    private val mockStory = Mockito.mock(StoryRepository::class.java)

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(mockStory)
    }

    @Test
    fun `when getStoriesMaps() is Called Should Not Null and Return Success`() {
        val expectedStory = MutableLiveData<Result<DataResponse>>()
        expectedStory.value = Result.Success(dummyMaps)

        Mockito.`when`(mockStory.getMaps()).thenReturn(expectedStory)

        val actualStory = mapsViewModel.getStoriesMaps().getOrAwaitValue()

        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Success<*>)
        assertEquals(dummyMaps.story.size, (actualStory as Result.Success).data.story.size)
    }

}