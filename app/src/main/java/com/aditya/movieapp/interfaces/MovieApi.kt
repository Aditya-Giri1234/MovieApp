package com.aditya.movieapp.interfaces

import com.aditya.movieapp.model.Resource
import com.aditya.movieapp.model.dataModel.MovieDetail
import com.aditya.movieapp.model.dataModel.MovieResponse
import com.aditya.movieapp.util.Constant
import com.aditya.movieapp.util.Constant.page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {


    @GET("{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Header(Constant.accept) accept:String=Constant.acceptValue,
        @Header(Constant.authorization) authorization: String = Constant.authorizationValue
    ): Response<MovieDetail>
    @GET(Constant.popular)
    suspend fun getMovieList(
        @Query(Constant.page) page: Int,
        @Header(Constant.accept) accept:String=Constant.acceptValue,
        @Header(Constant.authorization) authorization: String = Constant.authorizationValue
    ):Response<MovieResponse>


}