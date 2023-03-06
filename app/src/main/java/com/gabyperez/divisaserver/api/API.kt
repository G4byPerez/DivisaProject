package com.gabyperez.divisaserver.api

import retrofit2.Call
import retrofit2.http.GET

interface API {
    @get:GET("v6/064e537b97fd03303fa8e8ae/latest/USD")
    val posts : Call<Posts>
}