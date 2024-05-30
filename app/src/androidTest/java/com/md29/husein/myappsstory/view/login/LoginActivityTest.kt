package com.md29.husein.myappsstory.view.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.md29.husein.myappsstory.R
import com.md29.husein.myappsstory.data.networking.ApiConfig
import com.md29.husein.myappsstory.utils.EspressoIdlingResource
import com.md29.husein.myappsstory.view.main.MainActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    private val mockWebServer = MockWebServer()

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLoginLogoutScenario() {
        Intents.init()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.passwordEditText)).perform(typeText("12345678"))
        onView(withId(R.id.emailEditText)).perform(typeText("husein3@gmail.com"))
        onView(withId(R.id.loginButtonMain))
            .perform(scrollTo(), click())

        onView(withText(R.string.title_alert_dialog)).check(matches(isDisplayed()))
        onView(withText(R.string.message_positive_button)).perform(click())


        intended(hasComponent(MainActivity::class.java.name))
        onView(withId(R.id.textView_welcome_user)).check(matches(withText("Welcome !")))

        onView(withId(R.id.fab_logout)).perform(click())
        onView(withId(R.id.titleTextView)).check(matches(withText("Welcome to Your Stories!")))
    }
}