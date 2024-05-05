package com.aditya.movieapp.viewModel

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aditya.movieapp.interfaces.MovieApi
import com.aditya.movieapp.model.Resource
import com.aditya.movieapp.model.dataModel.Movie
import com.aditya.movieapp.repository.MovieRepository
import com.aditya.movieapp.util.Constant
import com.aditya.movieapp.util.Helper
import retrofit2.HttpException
import java.io.IOException


class MoviePagingSource(private val movieService: MovieApi) :
    PagingSource<Int, Movie>() {

    val TAG="MoviePagingSource"


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: 1
        return try {
            val response = movieService.getMovieList(position)
            Helper.isEndOfListFromUp=false
            if (!response.isSuccessful||response.body() == null) {
                Helper.customLog("MovieList","$TAG - load - pageNumber->$position - response Body null - response->$response")
                if(position==Constant.TOTAL_PAGE+1){
                    Helper.isEndOfListDown=true
                    Helper.isEndOfListFromUp=true
                    LoadResult.Error(Exception("An Error Occurred: Movie List is empty ."))
                }else{
                    LoadResult.Error(Exception("An Error Occurred: ${Constant.ErrorClass.NetworkFailure.message}"))
                }

            }
            else {
                Helper.customLog(
                    "MovieList",
                    "$TAG - load - pageNumber->$position - response Body not null - response->$response - body->${response.body()}"
                )
                response.body()!!.let {
                    if(it.movies.isEmpty()){
                        LoadResult.Error(Exception("An Error Occurred: Movie List is empty ."))
                    }
                    else{

                            LoadResult.Page(
                                data = it.movies,
                                prevKey = if (position == 1) null else position-1,
                                nextKey = if(position==Constant.TOTAL_PAGE+1) null else position+1
                            )
                    }
                }
            }

        } catch (e:IOException) {
            LoadResult.Error(Exception("An Error Occurred: ${Constant.ErrorClass.ConversionError.message}"))
        }catch (e:Exception){
            LoadResult.Error(Exception("An Error Occurred: ${Constant.ErrorClass.NetworkFailure.message}"))
        }
    }
}