package com.raywenderlich.marsrovers.services

import android.util.Log
import com.raywenderlich.marsrovers.models.PhotoList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object NASAPhotos {
    const val TAG = "NASAPhotos"
    val service : NASAApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        service = retrofit.create(NASAApi::class.java)
    }

    fun getPhotos(rover: String) : Call<PhotoList> {
        try {
            return service.getPhotos(rover)
        } catch (e : Exception) {
            Log.e(TAG, "Problems getting Photos", e)
            return ErrorCall(e)
        }
    }

}