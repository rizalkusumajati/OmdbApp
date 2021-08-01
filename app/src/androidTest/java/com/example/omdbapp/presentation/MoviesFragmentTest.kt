package com.example.omdbapp.presentation

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
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
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(BaseUrlModule::class)
class MoviesFragmentTest {
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
    fun getMovieListSuccess() {
        mockWebServer.dispatcher = MockWebServerDispatcher().RequestDispatcher()
        launchMoviesFragment()

        Espresso.onView(
            withId(R.id.info_text)
        ).check(
            matches(withText("Find Movies, TV shows and more ..."))
        )
        Espresso.onView(
            withId(R.id.rv_movie)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<MovieListViewHolder>(
                0,
                ViewActions.click()
            )
        )

        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.detailMovieFragment)
    }


    @Test
    fun getMovieListSearchSuccess() {
        mockWebServer.dispatcher = MockWebServerDispatcher().RequestDispatcher()
        launchMoviesFragment()
        Espresso.onView(
            withId(R.id.search_movie)
        ).perform(
            typeSearchViewText("lego")
        )

        Espresso.onView(
            withId(R.id.rv_movie)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<MovieListViewHolder>(
                0,
                ViewActions.click()
            )
        )

        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.detailMovieFragment)
    }

    fun typeSearchViewText(text: String?): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun getDescription(): String {
                return "Change view text"
            }

            override fun perform(uiController: UiController?, view: View) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }

    private fun launchMoviesFragment() {
        launchFragmentInHiltContainer<MoviesFragment> {
            navController.setGraph(R.navigation.main_nav)
            navController.setCurrentDestination(R.id.moviesFragment)
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    // The fragment’s view has just been created
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }
    }
}
