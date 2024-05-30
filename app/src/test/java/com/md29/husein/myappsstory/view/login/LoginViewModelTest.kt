package com.md29.husein.myappsstory.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.md29.husein.myappsstory.DataDummy
import com.md29.husein.myappsstory.MainDispatcherRule
import com.md29.husein.myappsstory.data.Result
import com.md29.husein.myappsstory.data.StoryRepository
import com.md29.husein.myappsstory.data.networking.DataResponse
import com.md29.husein.myappsstory.data.pref.UserModel
import com.md29.husein.myappsstory.data.pref.UserPreference
import com.md29.husein.myappsstory.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private val mockStory = Mockito.mock(StoryRepository::class.java)
    private val mockPref = Mockito.mock(UserPreference::class.java)
    private lateinit var loginViewModel: LoginViewModel
    private val dummyID = "MD29"
    private val dummyName = "Maulana"
    private val dummyEmail = "maulanahusein@gmail.com"
    private val dummyPass = "123456"
    private val dummyToken = "kwabkdahvysdaiukasvjws"
    private val dummyLoginSession = true


    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(mockPref, mockStory)
    }

    @Test
    fun `when user saved successful`() = runTest {
        loginViewModel.saveUser(UserModel(dummyToken, dummyID, dummyName, dummyLoginSession))
        Mockito.verify(mockPref)
            .saveUser(UserModel(dummyToken, dummyID, dummyName, dummyLoginSession))
    }

    @Test
    fun `when get login is successful and should not null`() {
        val expectedUser = MutableLiveData<Result<DataResponse>>()
        expectedUser.value = Result.Success(DataDummy.generateDummyResponse())

        Mockito.`when`(mockStory.login(dummyEmail, dummyPass)).thenReturn(expectedUser)
        val actualUser = loginViewModel.loginAcc(dummyEmail, dummyPass).getOrAwaitValue()

        Mockito.verify(mockStory).login(dummyEmail, dummyPass)
        assertTrue(actualUser is Result.Success)
        assertNotNull(actualUser)
    }
}