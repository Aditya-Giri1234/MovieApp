package com.aditya.movieapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.aditya.movieapp.model.dataModel.Movie
import com.aditya.movieapp.util.RetrofitHelper
import com.aditya.movieapp.viewModel.MoviePagingSource

class MovieRepository {

    suspend fun getMovieDetail(id: Int) = RetrofitHelper.getApiService().getMovieDetails(id)


     fun getMovieList(): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100
            ),
            pagingSourceFactory = {
                MoviePagingSource(RetrofitHelper.getApiService())
            }
        ).liveData
    }
}