package com.aditya.movieapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aditya.movieapp.model.Resource
import com.aditya.movieapp.model.dataModel.Movie
import com.aditya.movieapp.model.dataModel.MovieDetail
import com.aditya.movieapp.model.dataModel.MovieResponse
import com.aditya.movieapp.repository.MovieRepository
import com.aditya.movieapp.util.Constant
import com.aditya.movieapp.util.Helper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MovieViewModel(private val app: Application) : AndroidViewModel(app) {

    private val TAG = "MovieViewModel"
    private val repository = MovieRepository()
    private val movieDetail = MutableLiveData<Resource<MovieDetail>>()

    //    private val movieList=MutableLiveData<Resource<MovieResponse>>()
    val errorMessage = MutableLiveData<String>()
    val list: LiveData<PagingData<Movie>> = repository.getMovieList().cachedIn(viewModelScope)


    fun getMovieDetail(id: Int) = viewModelScope.launch {
        movieDetail.postValue(Resource.Loading())
        try {
            if (Helper.isInternetAvailable(app)) {
                val response = repository.getMovieDetail(id)
                Helper.customLog(
                    "Movie_Detail",
                    "$TAG - getMovieDetail - id->$id  - response->$response - body->${response.body()}"
                )
                movieDetail.postValue(handleMovieDetailResponse(response))
            } else {
                movieDetail.postValue(Resource.Error(Constant.ErrorClass.NoInternet.message))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    movieDetail.postValue(Resource.Error(Constant.ErrorClass.NetworkFailure.message))
                }

                else -> {
                    Helper.customLog(
                        "Movie_Detail",
                        "$TAG - getMovieDetail - id->$id  - error->${t.message}"
                    )
                    movieDetail.postValue(Resource.Error(Constant.ErrorClass.ConversionError.message))
                }
            }
        }

    }

    private fun handleMovieDetailResponse(response: Response<MovieDetail>): Resource<MovieDetail> {

        if (response.isSuccessful) {
            response.body()?.let { data ->
                return Resource.Success(data)
            }
        }

        return Resource.Error(response.message())

    }


    fun observeMovieDetail(): LiveData<Resource<MovieDetail>> {
        return movieDetail
    }


}