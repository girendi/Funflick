package com.girendi.funflick.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.girendi.funflick.R
import com.girendi.funflick.core.data.UiState
import com.girendi.funflick.core.domain.model.Genre
import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.ui.SimpleRecyclerAdapter
import com.girendi.funflick.databinding.ActivityMainBinding
import com.girendi.funflick.databinding.ItemListGenreBinding
import com.girendi.funflick.databinding.ItemListMovieHorizontalBinding
import com.girendi.funflick.presentation.detail.MovieDetailActivity
import com.girendi.funflick.presentation.list.MovieListActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterGenre: SimpleRecyclerAdapter<Genre>
    private lateinit var adapterMovie: SimpleRecyclerAdapter<Movie>
    private val viewModelMain: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Explore Movies"

        setupRecyclerView()
        setupObserver()
        setupOnClick()
    }

    private fun setupOnClick() {
        binding.viewAll.setOnClickListener {
            startActivity(Intent(this@MainActivity, MovieListActivity::class.java))
        }
    }

    private fun setupObserver() {
        viewModelMain.genres.observe(this) { genres ->
            adapterGenre.setListItem(genres)
        }
        viewModelMain.movies.observe(this) { movies ->
            adapterMovie.setListItem(movies)
        }
        viewModelMain.uiState.observe(this) { state ->
            handleUiState(state)
        }
    }

    private fun setupRecyclerView() {
        adapterGenre = SimpleRecyclerAdapter(
            context = this,
            layoutResId = R.layout.item_list_genre,
            bindViewHolder = { view, item ->
                val itemBinding = ItemListGenreBinding.bind(view)
                itemBinding.tvGenre.text = item.name
                Glide.with(this)
                    .load(viewModelMain.getIcon(item.id))
                    .into(itemBinding.ivGenre)
                itemBinding.root.setOnClickListener {
                    val intent = Intent(this@MainActivity, MovieListActivity::class.java)
                    intent.putExtra(MovieListActivity.EXTRA_GENRE, item)
                    startActivity(intent)
                }
            }
        )
        adapterMovie = SimpleRecyclerAdapter(
            context = this,
            layoutResId = R.layout.item_list_movie_horizontal,
            bindViewHolder = { view, item ->
                val itemBinding = ItemListMovieHorizontalBinding.bind(view)
                itemBinding.tvName.text = item.title
                Glide.with(this)
                    .load(item.posterPath?.let { path ->
                        viewModelMain.getBackdropPathImage(
                            path
                        )
                    })
                    .into(itemBinding.ivMovie)
                itemBinding.root.setOnClickListener {
                    val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
                    intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, item.id)
                    startActivity(intent)
                }
            }
        )

        binding.rvGenre.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterGenre
        }
        binding.rvTrending.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = adapterMovie
        }
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
}