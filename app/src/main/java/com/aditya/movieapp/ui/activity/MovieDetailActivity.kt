package com.aditya.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isGone
import com.aditya.movieapp.R
import com.aditya.movieapp.databinding.ActivityMovieDetailBinding
import com.aditya.movieapp.model.Resource
import com.aditya.movieapp.model.dataModel.MovieDetail
import com.aditya.movieapp.util.Constant
import com.aditya.movieapp.util.Helper
import com.aditya.movieapp.viewModel.MovieViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import java.text.NumberFormat
import java.util.Locale

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding

    val viewModel by viewModels<MovieViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initUi()
        subscribeToObserver()

    }

    private fun initUi() {
viewModel.getMovieDetail(intent.getIntExtra(Constant.Movie_Id ,0))
    }

    private fun subscribeToObserver() {
        viewModel.observeMovieDetail().observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                            response.data?.let {
                                setData(it)
                                return@observe
                            }
                    showError("No Data Found !")
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Error -> {
                    hideProgressBar()

                    response.message?.let {
                        showError(it)
                    }

                }
            }
        }
    }

    private fun setData(it: MovieDetail) {
        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)

        binding.movieTitle.text = it.title
        binding.movieTagline.text = it.tagline
        binding.movieReleaseDate.text = it.releaseDate
        binding.movieRating.text = it.voteAverage.toString()
        binding.movieRuntime.text = it.runtime.toString() + " minutes"
        binding.movieBudget.text = formatCurrency.format(it.budget)
        binding.movieRevenue.text = formatCurrency.format(it.revenue)
        binding.movieOverview.text = it.overview

        val moviePosterURL = Constant.posterBaseUrl + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply (RequestOptions.bitmapTransform(RoundedCorners(14)))
            .into(binding.ivMoviePoster)
    }

    private fun showError(it: String) {
        binding.txtError.isGone = false
        binding.txtError.text = "An Error Occurred: $it"
    }

    private fun hideProgressBar() {
        binding.progressBarPopular.isGone = true
        binding.linearLayout.isGone = false
    }

    private fun showProgressBar() {
        binding.progressBarPopular.isGone = false
        binding.linearLayout.isGone = true
    }
}