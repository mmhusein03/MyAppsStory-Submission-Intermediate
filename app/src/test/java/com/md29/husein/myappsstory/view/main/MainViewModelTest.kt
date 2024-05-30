package com.md29.husein.myappsstory.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.md29.husein.myappsstory.DataDummy
import com.md29.husein.myappsstory.MainDispatcherRule
import com.md29.husein.myappsstory.data.StoryRepository
import com.md29.husein.myappsstory.data.adapter.StoriesAdapter
import com.md29.husein.myappsstory.data.networking.ListStories
import com.md29.husein.myappsstory.data.pref.UserPreference
import com.md29.husein.myappsstory.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel
    private val mockStory = Mockito.mock(StoryRepository::class.java)
    private val mockPref = Mockito.mock(UserPreference::class.java)

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(mockPref, mockStory)
    }

    @Test
    fun `when Get Story is successful`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<ListStories> = StoryPagedTestSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStories>>()
        expectedStory.value = data

        Mockito.`when`(mockStory.getStory()).thenReturn(expectedStory)

        val actualStory: PagingData<ListStories> =
            mainViewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val emptyLiveData = MutableLiveData<PagingData<ListStories>>()
        emptyLiveData.value = PagingData.empty()

        Mockito.`when`(mockStory.getStory()).thenReturn(emptyLiveData)

        val actualStory: PagingData<ListStories> =
            mainViewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }

    @Test
    fun `when getUser() is called should no null and Return Success`() = runTest {
        val expectedResponse = flowOf(DataDummy.generateDummyUserModel())

        Mockito.`when`(mockPref.getUser()).thenReturn(expectedResponse)

        mainViewModel.getUser().collect {
            assertNotNull(it.token)
            assertEquals(DataDummy.generateDummyUserModel().token, it.token)
        }

        Mockito.verify(mockPref).getUser()
    }

    @Test
    fun `when logout() is called should Success`() = runTest {
        mainViewModel.logout()
        Mockito.verify(mockPref).logout()
    }

}


class StoryPagedTestSource : PagingSource<Int, LiveData<List<ListStories>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStories>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStories>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<ListStories>): PagingData<ListStories> {
            return PagingData.from(items)
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}