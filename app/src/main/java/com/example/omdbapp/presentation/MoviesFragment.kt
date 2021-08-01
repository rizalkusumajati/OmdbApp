package com.example.omdbapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.recyclical.datasource.emptyDataSource
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.example.omdbapp.EndlessScrollListener
import com.example.omdbapp.OnLoadMoreListener
import com.example.omdbapp.R
import com.example.omdbapp.databinding.FragmentMoviesBinding
import com.example.omdbapp.domain.MovieHeader
import com.example.omdbapp.domain.UIState
import com.example.omdbapp.loadUrl
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [MoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()
    private val movieDataSource = emptyDataSource()
    lateinit var endlessScrollListener: EndlessScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDisplay()
        setupObserver()
        viewModel.getDetailMovie("Marvel")
    }

    private fun setupDisplay() {
        binding.rvMovie.setup {
            withLayoutManager(GridLayoutManager(requireContext(), 2))
            withDataSource(movieDataSource)
            withItem<MovieHeader, MovieListViewHolder>(R.layout.item_movie) {
                onBind(::MovieListViewHolder) { _, item ->
                    poster.loadUrl(item.poster)
                }.onClick {
                    val action = MoviesFragmentDirections.goToDetail(item.imdbID)
                    findNavController().navigate(action)
                }
            }

        }

        endlessScrollListener = EndlessScrollListener(GridLayoutManager(requireContext(), 2))
        endlessScrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                viewModel.incrementPageCount()
                viewModel.getDetailMovie(viewModel.searchKeyGlobal)
                Log.d("Scroll", "Down")
            }

        })

        binding.rvMovie.addOnScrollListener(endlessScrollListener)

        binding.searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let {
                    viewModel.resetPageCount()
                    movieDataSource.clear()
                    viewModel.getDetailMovie(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun setupObserver() {
        viewModel.listMovie.observe(viewLifecycleOwner) { uiState ->

            when (uiState) {
                UIState.Empty -> Log.d("MOVIE", "empty")
                is UIState.Error -> {
                    uiState.showToast(requireContext())
                }
                UIState.Loading -> {
                    Log.d("MOVIE", "Loading")
                }
                is UIState.SuccessFromRemote -> {
                    with(uiState.getData()) {
                        val movieData = this.listHeader
                        movieDataSource.addAll(movieData)
                        if (viewModel.pageCount > 1) {
                            endlessScrollListener.setLoaded()
                        }
                    }
                }
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
