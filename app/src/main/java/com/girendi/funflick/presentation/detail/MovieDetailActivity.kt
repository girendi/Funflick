package com.girendi.funflick.presentation.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.girendi.funflick.R
import com.girendi.funflick.core.data.UiState
import com.girendi.funflick.core.domain.model.Movie
import com.girendi.funflick.core.domain.model.Review
import com.girendi.funflick.core.ui.SimpleRecyclerAdapter
import com.girendi.funflick.databinding.ActivityMovieDetailBinding
import com.girendi.funflick.databinding.ItemListReviewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var adapterReview: SimpleRecyclerAdapter<Review>
    private val viewModelDetail: MovieDetailViewModel by viewModel()
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        movieId = intent.getIntExtra(EXTRA_MOVIE_ID, 0)

        setupRecyclerView()
        setupObserver()
        setupOnClick()
    }

    private fun setupRecyclerView() {
        adapterReview = SimpleRecyclerAdapter(
            context = this@MovieDetailActivity,
            layoutResId = R.layout.item_list_review,
            bindViewHolder = { view, item ->
                val itemBinding = ItemListReviewBinding.bind(view)
                itemBinding.tvName.text = item.author
                itemBinding.tvDate.text = item.createdAt
                itemBinding.tvContent.text = item.content
            }
        )
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(this@MovieDetailActivity)
            adapter = adapterReview
        }

        binding.rvReview.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!binding.rvReview.canScrollVertically(1)) {
                        loadData()
                    }
                }
            }
        )
    }

    private fun loadData() {
        viewModelDetail.fetchReviewList(movieId)
    }

    private fun setupOnClick() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.ivPlay.setOnClickListener {
            viewModelDetail.fetchVideo(movieId)
        }
    }

    private fun setupObserver() {
        viewModelDetail.uiState.observe(this) { state ->
            handleUiState(state)
        }
        viewModelDetail.movie.observe(this) { movie ->
            showMovieDetail(movie)
        }
        viewModelDetail.reviews.observe(this) { reviews ->
            adapterReview.setListItem(reviews)
        }
        viewModelDetail.video.observe(this) { video ->
            if (video != null) playYoutubeVideo(video.key)
        }
        viewModelDetail.fetchMovieDetail(movieId)
        loadData()
    }

    @SuppressLint("SetTextI18n")
    private fun showMovieDetail(movie: Movie) {
        binding.tvTitle.text = movie.title
        binding.tvDate.text = movie.releaseDate
        binding.tvDuration.text = "${movie.runtime} Minutes"
        binding.tvOverview.text = movie.overview

        val genres = movie.genres
        val listGenre = genres?.joinToString(separator = ", ") { it.name }
        binding.tvGenre.text = listGenre

        Glide.with(this)
            .load(movie.backdropPath?.let {
                viewModelDetail.getBackdropPathImage(it)
            })
            .into(binding.ivBackdrop)

        Glide.with(this)
            .load(movie.posterPath?.let {
                viewModelDetail.getBackdropPathImage(it)
            })
            .into(binding.ivPoster)
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

    private fun playYoutubeVideo(videoKey: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoKey"))
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }
}