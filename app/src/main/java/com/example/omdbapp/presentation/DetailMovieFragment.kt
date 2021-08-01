package com.example.omdbapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.example.omdbapp.R
import com.example.omdbapp.databinding.FragmentDetailMovieBinding
import com.example.omdbapp.domain.MovieDetail
import com.example.omdbapp.domain.UIState
import com.example.omdbapp.loadUrl
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [DetailMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailMovieViewModel by viewModels()
    private val args: DetailMovieFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        args.movieId?.let {
            viewModel.getDetailMovie(it)
        }

    }

    private fun setupDisplay(movieDetail: MovieDetail){
        with(binding){
            ivPoster.loadUrl(movieDetail.poster)
            tvPlot.text = movieDetail.plot
            titleMovie.text = "${movieDetail.title} (${movieDetail.year})"
            tvRating.text = "${movieDetail.imdbRating.toDoubleOrNull() ?: 0}"

            val genre = movieDetail.genre.split(",").toList()
            genre.let {
                rvGenre.setup {
                    withLayoutManager(LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false))
                    withDataSource(dataSourceTypedOf(genre))
                    withItem<String, DetailMovieViewHolder>(R.layout.item_genre){
                        onBind(::DetailMovieViewHolder){ _, item ->
                            chip.text = item
                        }
                    }
                }
            }

            val infoDetail = listOf<InfoDetailMovie>(
                InfoDetailMovie("Director", movieDetail.director),
                InfoDetailMovie("Writer", movieDetail.writer),
                InfoDetailMovie("Actors", movieDetail.actors),
                InfoDetailMovie("Duration", movieDetail.runtime),
                InfoDetailMovie("Language", movieDetail.language),
            )

            rvInfo.setup {
                withLayoutManager(LinearLayoutManager(requireContext()))
                withDataSource(dataSourceOf(infoDetail))
                withItem<InfoDetailMovie, DetailMovieInfoHolder>(R.layout.item_info){
                    onBind(::DetailMovieInfoHolder){ _, item ->
                        tvTitle.text = item.title
                        tvDetail.text = item.detail
                    }
                }
            }

            rvInfo.setHasFixedSize(true)

        }
    }

    private fun setupObserver(){
        viewModel.detailMovie.observe(viewLifecycleOwner) { uiState ->

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
                        Log.d("MOVIE", this.toString())
                        setupDisplay(this)
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
