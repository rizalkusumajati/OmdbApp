package com.example.omdbapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.omdbapp.TestCoroutineRule
import com.example.omdbapp.data.repository.MovieRepository
import com.example.omdbapp.domain.MovieDetail
import com.example.omdbapp.domain.UIState
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DetailMovieViewModelTest {
    @get:Rule
    val testInstantTaskRules: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var repository: MovieRepository

    private lateinit var movieDetailViewModel: DetailMovieViewModel

    @MockK
    private lateinit var uiStateMovieListObserver: Observer<UIState<MovieDetail>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        movieDetailViewModel = DetailMovieViewModel(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testGetDetailMovieLoading() {
        //given
        val omdbKey = "tt4154664"
        val loadingValue = UIState.Loading
        val captureData: MutableList<UIState<MovieDetail>> = mutableListOf()
        testCoroutineRule.runBlockingTest {
            val outputFlow: Flow<UIState<MovieDetail>> = flow {
                emit(loadingValue)
            }
            coEvery { repository.getMovieDetail(any()) } returns outputFlow
            movieDetailViewModel.detailMovie.observeForever(uiStateMovieListObserver)
            //when
            movieDetailViewModel.getDetailMovie(omdbKey)

            //then
            verify(
                verifyBlock = { uiStateMovieListObserver.onChanged(capture(captureData)) },
                atLeast = 1
            )
            Truth.assertThat(captureData[0]).isInstanceOf(UIState.Loading::class.java)
            Truth.assertThat(captureData[0]).isEqualTo(loadingValue)
            Truth.assertThat(movieDetailViewModel.detailMovie.value).isEqualTo(loadingValue)
        }
    }

    @Test
    fun testGetDetailMovieError() {
        //given
        val omdbKey = "tt4154664"
        val errorValue = UIState.Error("error message")
        val captureData: MutableList<UIState<MovieDetail>> = mutableListOf()
        testCoroutineRule.runBlockingTest {
            val outputFlow: Flow<UIState<MovieDetail>> = flow {
                emit(errorValue)
            }
            coEvery { repository.getMovieDetail(any()) } returns outputFlow
            movieDetailViewModel.detailMovie.observeForever(uiStateMovieListObserver)
            //when
            movieDetailViewModel.getDetailMovie(omdbKey)

            //then
            verify(
                verifyBlock = { uiStateMovieListObserver.onChanged(capture(captureData)) },
                atLeast = 1
            )
            Truth.assertThat(captureData[0]).isInstanceOf(UIState.Error::class.java)
            Truth.assertThat(captureData[0]).isEqualTo(errorValue)
            Truth.assertThat(movieDetailViewModel.detailMovie.value).isEqualTo(errorValue)
        }
    }

    @Test
    fun getDetailMovieSuccess() {
        //given
        val omdbKey = "tt4154664"
        val movieDetail =
            MovieDetail(
                metascore = "",
                boxOffice = "",
                website = "",
                imdbRating = "",
                imdbVotes = "",
                ratings = emptyList(),
                runtime = "",
                language = "",
                rated = "",
                production = "",
                released = "",
                imdbID = "",
                plot = "",
                director = "",
                title = "",
                actors = "",
                response = "",
                type = "",
                awards = "",
                dVD = "",
                year = "",
                poster = "",
                country = "",
                genre = "",
                writer = ""
            )

        val successValue = UIState.SuccessFromRemote(movieDetail)
        val captureData: MutableList<UIState<MovieDetail>> = mutableListOf()
        testCoroutineRule.runBlockingTest {
            val outputFlow: Flow<UIState<MovieDetail>> = flow {
                emit(successValue)
            }
            coEvery { repository.getMovieDetail(any()) } returns outputFlow
            movieDetailViewModel.detailMovie.observeForever(uiStateMovieListObserver)

            //when
            movieDetailViewModel.getDetailMovie(omdbKey)

            //then
            verify(
                verifyBlock = { uiStateMovieListObserver.onChanged(capture(captureData)) },
                atLeast = 1
            )
            Truth.assertThat(captureData[0]).isInstanceOf(UIState.SuccessFromRemote::class.java)
            Truth.assertThat(captureData[0]).isEqualTo(successValue)
            Truth.assertThat(movieDetailViewModel.detailMovie.value).isEqualTo(successValue)
        }
    }
}
