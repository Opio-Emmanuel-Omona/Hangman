package com.example.hangman

import retrofit2.Call
import retrofit2.http.GET


interface GetDataService {

    @get:GET("/photos")
    val allPhotos: Call<List<RetroPhoto>>
}