package com.raywenderlich.marsrovers.services

import com.raywenderlich.marsrovers.models.PhotoList
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 */
class ErrorCall(val exception: Exception) : Call<PhotoList> {
    override fun cancel() {
    }

    override fun request(): Request? {
        return null
    }

    override fun clone(): Call<PhotoList> {
        return this
    }

    override fun isCanceled(): Boolean {
        return true
    }

    override fun enqueue(callback: Callback<PhotoList>) {
        callback.onFailure(this, exception)
    }

    override fun isExecuted(): Boolean {
        return true
    }

    override fun execute(): Response<PhotoList>? {
        return null
    }
}