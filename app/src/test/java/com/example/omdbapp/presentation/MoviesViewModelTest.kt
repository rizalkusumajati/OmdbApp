package com.example.omdbapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.omdbapp.TestCoroutineRule
import com.example.omdbapp.data.repository.MovieRepository
import com.example.omdbapp.domain.MovieHeader
import com.example.omdbapp.domain.MovieSearch
import com.example.omdbapp.domain.UIState
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase
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
class MoviesViewModelTest {
    @get:Rule
    val testInstantTaskRules: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var repository: MovieRepository

    private lateinit var moviesViewModel: MoviesViewModel

    @MockK
    private lateinit var uiStateMovieListObserver: Observer<UIState<MovieSearch>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        moviesViewModel = MoviesViewModel(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
    @Test
    fun testResetPageCount() {
        //given
        moviesViewModel.incrementPageCount()
        //when
        moviesViewModel.resetPageCount()
        //then
        Truth.assertThat(moviesViewModel.pageCount).isEqualTo(1)
    }
    @Test
    fun testIncrementPageCount() {
        //given
        moviesViewModel.resetPageCount()
        //when
        moviesViewModel.incrementPageCount()
        //then
        Truth.assertThat(moviesViewModel.pageCount).isEqualTo(2)
    }

    @Test
    fun testGetDetailMovieLoading(){
        //given
        val searchKey = "lego"
        val loadingValue = UIState.Loading
        val captureData:  MutableList<UIState<MovieSearch>> = mutableListOf()
        testCoroutineRule.runBlockingTest {
            val outputFlow : Flow<UIState<MovieSearch>> = flow {
                emit(loadingValue)
            }
            coEvery { repository.getMovieList(any(), any()) } returns outputFlow
            moviesViewModel.listMovie.observeForever(uiStateMovieListObserver)
            //when
            moviesViewModel.getDetailMovie(searchKey)

            //then
            verify (
                verifyBlock = { uiStateMovieListObserver.onChanged(capture(captureData)) },
                atLeast = 1
            )
            Truth.assertThat(moviesViewModel.searchKeyGlobal).isEqualTo(searchKey)
            Truth.assertThat(captureData[0]).isInstanceOf(UIState.Loading::class.java)
            Truth.assertThat(captureData[0]).isEqualTo(loadingValue)
            Truth.assertThat(moviesViewModel.listMovie.value).isEqualTo(loadingValue)
        }
    }
    @Test
    fun testGetDetailMovieError(){
        //given
        val searchKey = "lego"
        val errorValue = UIState.Error("error")
        val captureData:  MutableList<UIState<MovieSearch>> = mutableListOf()
        testCoroutineRule.runBlockingTest {
            val outputFlow : Flow<UIState<MovieSearch>> = flow {
                emit(errorValue)
            }
            coEvery { repository.getMovieList(any(), any()) } returns outputFlow
            moviesViewModel.listMovie.observeForever(uiStateMovieListObserver)
            //when
            moviesViewModel.getDetailMovie(searchKey)

            //then
            verify (
                verifyBlock = { uiStateMovieListObserver.onChanged(capture(captureData)) },
                atLeast = 1
            )
            Truth.assertThat(moviesViewModel.searchKeyGlobal).isEqualTo(searchKey)
            Truth.assertThat(captureData[0]).isInstanceOf(UIState.Error::class.java)
            Truth.assertThat(captureData[0]).isEqualTo(errorValue)
            Truth.assertThat(moviesViewModel.listMovie.value).isEqualTo(errorValue)
        }
    }

    @Test
    fun testGetDetailMovieSuccess() {
        //given
        val searchKey = "lego"
        val movieHeaders = listOf<MovieHeader>(
            MovieHeader(
                title = "Captain Marvel",
                year = "2019",
                imdbID = "tt4154664",
                type = "movie",
                poster = "https://m.media-amazon.com/images/M/MV5BMTE0YWFmOTMtYTU2ZS00ZTIxLWE3OTEtYTNiYzBkZjViZThiXkEyXkFqcGdeQXVyODMzMzQ4OTI@._V1_SX300.jpg"
            )
        )
        val movieSearch =
            MovieSearch(
                totalResults = "131",
                response = "True",
                listHeader = movieHeaders
            )
        val successValue = UIState.SuccessFromRemote(movieSearch)
        val captureData:  MutableList<UIState<MovieSearch>> = mutableListOf()
        testCoroutineRule.runBlockingTest {
            val outputFlow : Flow<UIState<MovieSearch>> = flow {
                emit(successValue)
            }
            coEvery { repository.getMovieList(any(), any()) } returns outputFlow
            moviesViewModel.listMovie.observeForever(uiStateMovieListObserver)

            //when
            moviesViewModel.getDetailMovie(searchKey)

            //then
            verify (
                verifyBlock = { uiStateMovieListObserver.onChanged(capture(captureData)) },
                atLeast = 1
            )
            Truth.assertThat(moviesViewModel.searchKeyGlobal).isEqualTo(searchKey)
            Truth.assertThat(captureData[0]).isInstanceOf(UIState.SuccessFromRemote::class.java)
            Truth.assertThat(captureData[0]).isEqualTo(successValue)
            Truth.assertThat(moviesViewModel.listMovie.value).isEqualTo(successValue)
        }
    }
}
