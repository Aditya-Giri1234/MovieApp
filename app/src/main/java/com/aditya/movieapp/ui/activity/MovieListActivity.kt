package com.aditya.movieapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.aditya.movieapp.R
import com.aditya.movieapp.adapter.ListLoadingStateAdapter
import com.aditya.movieapp.adapter.MoviePagerAdapter
import com.aditya.movieapp.databinding.ActivityMovieListBinding
import com.aditya.movieapp.interfaces.ConnectivityObserver
import com.aditya.movieapp.util.Constant
import com.aditya.movieapp.util.Helper
import com.aditya.movieapp.util.other.FooterDecoration
import com.aditya.movieapp.util.other.NetworkConnectivityObserver
import com.aditya.movieapp.viewModel.MovieViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MovieListActivity : AppCompatActivity() {

    val TAG = "MovieListActivity"
    private lateinit var binding: ActivityMovieListBinding
    private val viewModel by viewModels<MovieViewModel>()
    private val pagerAdapter by lazy {
        MoviePagerAdapter() {
            Intent(this, MovieDetailActivity::class.java).putExtra(Constant.Movie_Id, it)
                .also(::startActivity)
        }
    }
    private var isFirstTimeLoad = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        subscribeToObserver()
        //Internet checking Losing and lost states
        val connectivityObserver = NetworkConnectivityObserver(applicationContext)
        connectivityObserver.observe().onEach {
            when (it.name) {
                ConnectivityObserver.NetworkStatus.Available.name -> {
                    Helper.customLog(
                        "Network",
                        "$TAG - onCreate - connectivityObserver - Available "
                    )
                    if (isFirstTimeLoad && pagerAdapter.itemCount == 0) {
                        pagerAdapter.refresh()
                        pagerAdapter.retry()
                    }
                }

                ConnectivityObserver.NetworkStatus.Losing.name -> {
                    Helper.customLog(
                        "Network",
                        "$TAG - onCreate - connectivityObserver - Losing .. "
                    )
                }

                ConnectivityObserver.NetworkStatus.Lost.name -> {
                    Helper.customLog("Network", "$TAG - onCreate - connectivityObserver - Lost ")
                }

                ConnectivityObserver.NetworkStatus.Unavailable.name -> {
                    Helper.customLog(
                        "Network",
                        "$TAG - onCreate - connectivityObserver - Un Available "
                    )
                }
            }
        }.launchIn(lifecycleScope)

    }

    private fun subscribeToObserver() {
        viewModel.errorMessage.observe(this) { error ->
            if (Helper.isInternetAvailable(app = application)) {
                showError(error)
            } else {
                showError(Constant.ErrorClass.NoInternet.message)
            }

        }
        viewModel.list.observe(this) {
            it?.let {
                pagerAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun initUi() {


        binding.rvMovieList.apply {
            setHasFixedSize(true)

            // Set layout manager
            val layoutManagerCustom =
                GridLayoutManager(this@MovieListActivity, 3, GridLayoutManager.VERTICAL, false)
            layoutManagerCustom.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    adapter?.apply {
                        getItemViewType(position).let {
                            return if (it == 1) 1 else 3
                        }
                    }
                    return -1
                }
            }
            layoutManager = layoutManagerCustom


            // Set pageAdapter
            adapter = pagerAdapter.withHeaderAndFooter(
                header = ListLoadingStateAdapter {
                    pagerAdapter.retry()
                },
                footer = ListLoadingStateAdapter {
                    pagerAdapter.retry()
                }
            )

            // Set load state first time load in recycleView
            pagerAdapter.apply {
                addLoadStateListener { loadState ->
                    // show empty list

                    if (isFirstTimeLoad) {
                        if (loadState.refresh is LoadState.Loading ||
                            loadState.append is LoadState.Loading
                        ) {
                            showLoading()
                        } else {
                            isFirstTimeLoad = false
                            hideLoading()
                            // If we have an error, show a toast
                            val errorState = when {
                                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                                else -> null
                            }

                            errorState?.let {
                                isFirstTimeLoad = true
                                viewModel.errorMessage.postValue(it.error.message)
                            }
                        }

                    }
                }
            }


        }
    }

    fun showLoading() {
        binding.txtErrorPopular.isGone = true
        binding.progressBarPopular.isVisible = true
    }

    fun hideLoading() {
        binding.progressBarPopular.isVisible = false
    }

    fun showError(error: String) {
        binding.txtErrorPopular.isGone = false
        binding.txtErrorPopular.text = error
    }
}