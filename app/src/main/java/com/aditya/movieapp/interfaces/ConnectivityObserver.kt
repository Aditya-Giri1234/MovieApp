package com.aditya.movieapp.interfaces

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<NetworkStatus>


    enum class NetworkStatus {
        Available,Unavailable,Losing, Lost
    }
}