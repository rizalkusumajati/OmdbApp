package com.example.omdbapp.presentation

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.example.omdbapp.MockWebServerDispatcher
import com.example.omdbapp.R
import com.example.omdbapp.TestIdlingResource
import com.example.omdbapp.data.di.BaseUrlModule
import com.example.omdbapp.launchFragmentInHiltContainer
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(BaseUrlModule::class)
class DetailMovieFragmentTest {
    private val mockWebServer = MockWebServer()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    private var idlingResource: IdlingResource? = null

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer.start(8080)
        idlingResource = TestIdlingResource.countingIdlingResource
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        idlingResource?.let {
            IdlingRegistry.getInstance().unregister(it)
        }
    }

    @Test
    fun getDetailMovieSuccess() {
        mockWebServer.dispatcher = MockWebServerDispatcher().RequestDispatcher()
        launchDetailMovieFragment()

        Espresso.onView(
            ViewMatchers.withId(R.id.rv_genre)
        ).check(
            ViewAssertions.matches((ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        )

        Espresso.onView(
            ViewMatchers.withId(R.id.rv_info)
        ).check(
            ViewAssertions.matches((ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        )

        Espresso.onView(
            ViewMatchers.withId(R.id.iv_poster)
        ).check(
            ViewAssertions.matches(isDisplayed())
        )

        Espresso.onView(
            ViewMatchers.withId(R.id.tv_plot)
        ).check(
            ViewAssertions.matches(isDisplayed())
        )

        Espresso.onView(
            ViewMatchers.withId(R.id.tv_rating)
        ).check(
            ViewAssertions.matches(isDisplayed())
        )

    }

    private fun launchDetailMovieFragment() {
        launchFragmentInHiltContainer<DetailMovieFragment>(
            bundleOf("movieId" to "b9bd48a6")
        ) {
            navController.setGraph(R.navigation.main_nav)
            navController.setCurrentDestination(R.id.detailMovieFragment)
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    // The fragment’s view has just been created
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }
    }
}
