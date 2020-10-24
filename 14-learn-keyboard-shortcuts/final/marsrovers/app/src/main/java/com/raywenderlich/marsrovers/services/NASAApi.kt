package com.raywenderlich.marsrovers.services

import com.raywenderlich.marsrovers.models.PhotoList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *
 */
interface NASAApi {
    @GET("mars-photos/api/v1/rovers/{rover}/photos?sol=1000&api_key=MfrPknQajnh5yQI5ozcUkORXjfGYJIDKCirp6DKa")
    fun getPhotos(@Path("rover") rover: String) : Call<PhotoList>
}