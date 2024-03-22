package com.girendi.funflick.presentation.list

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.girendi.funflick.R
import com.girendi.funflick.core.data.UiState
import com.girendi.funflick.core.domain.model.Genre
import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.ui.SimpleRecyclerAdapter
import com.girendi.funflick.databinding.ActivityMovieListBinding
import com.girendi.funflick.databinding.ItemListMovieVerticalBinding
import com.girendi.funflick.presentation.detail.MovieDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieListActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapterMovie: SimpleRecyclerAdapter<Movie>
    private val viewModelList: MovieListViewModel by viewModel()

    private var genre: Genre? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        genre = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_GENRE, Genre::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_GENRE)
        }

        if (genre != null) {
            supportActionBar?.title = genre?.name
        } else {
            supportActionBar?.title = getString(R.string.in_theaters_now)
        }

        setupRecyclerView()
        setupObserver()

    }

    private fun setupObserver() {
        viewModelList.movies.observe(this) { movies ->
            adapterMovie.setListItem(movies)
        }
        viewModelList.uiState.observe(this) { state ->
            handleUiState(state)
        }
        loadData()
    }

    private fun loadData() {
        if (genre != null) {
            viewModelList.fetchMovieByGenre(genre?.id.toString())
        } else {
            viewModelList.fetchMovieList()
        }
    }

    private fun setupRecyclerView() {
        adapterMovie = SimpleRecyclerAdapter(
            context = this,
            layoutResId = R.layout.item_list_movie_vertical,
            bindViewHolder = { view, item ->
                val itemBinding = ItemListMovieVerticalBinding.bind(view)
                itemBinding.tvName.text = item.title
                val maxLength = 150
                val description = item.overview!!
                val shortenedText = if (description.length > maxLength) description.substring(0, maxLength) + "..." else description
                itemBinding.tvDescription.text = shortenedText
                itemBinding.tvDate.text = item.releaseDate
                Glide.with(this)
                    .load(item.posterPath?.let { path ->
                        viewModelList.getBackdropPathImage(
                            path
                        )
                    })
                    .into(itemBinding.ivMovie)
                itemBinding.root.setOnClickListener {
                    val intent = Intent(this@MovieListActivity, MovieDetailActivity::class.java)
                    intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, item.id)
                    startActivity(intent)
                }
            }
        )
        binding.rvMovie.apply {
            layoutManager = LinearLayoutManager(this@MovieListActivity)
            adapter = adapterMovie
        }
        binding.rvMovie.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!binding.rvMovie.canScrollVertically(1)) {
                        loadData()
                    }
                }
            }
        )
    }

    private fun handleUiState(state: UiState) = when (state) {
        is UiState.Success -> {
            showLoading(false)
        }
        is UiState.Error -> {
            showError(state.message)
            showLoading(false)
        }
        is UiState.Loading -> {
            showLoading(true)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_GENRE = "extra_genre"
    }
}