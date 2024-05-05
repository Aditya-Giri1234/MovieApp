package com.aditya.movieapp.util

import android.os.Message

object Constant {

     const val accept="accept"
    const val acceptValue="application/json"
    const val authorization="Authorization"
    const val authorizationValue="Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMTc2ZTE1ZTYzOWRkOTY1YmViNmQ4NTZkN2YzOTFlMiIsInN1YiI6IjY1NWQ5ZjVlOWQ4OTM5MDEwMDMzMDg4NSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.txLoIZP35vQPAAMR0k-Y4Gu3RB0U_ZO8jxk_DlZchZI"
    const val baseUrl="https://api.themoviedb.org/3/movie/"
    const val posterBaseUrl="https://image.tmdb.org/t/p/w342"
    const val Movie_Id="movieId"
    const val popular="popular"
    const val page="page"
    const val TOTAL_PAGE=500

//    const val network_failure="Network Failure !"
//    const val no_internet="No Internet !"
//    const val conversion_error="Conversion Error !""

    enum class ErrorClass(val message: String){
        NetworkFailure("Network Failure !"),
        NoInternet("No Internet !"),
        ConversionError("Conversion Error !")
    }

}