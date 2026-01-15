package com.example.bookshelf.network

import com.example.bookshelf.network.model.SearchResponse
import com.example.bookshelf.network.model.VolumeDetailsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// This is BaseUrl of
private const val BASE_URL = "https://www.googleapis.com/books/v1/"


// Retrofit instance with Gson converter
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface BooksApiService {

    // Search for books: /volumes?q=jazz+history
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String
    ): SearchResponse

    // Get details for a single volume: /volumes/{volumeId}
    @GET("volumes/{volumeId}")
    suspend fun getBookDetails(
        @Path("volumeId") volumeId: String
    ): VolumeDetailsResponse
}

object BooksApi {
    val service: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }
}
