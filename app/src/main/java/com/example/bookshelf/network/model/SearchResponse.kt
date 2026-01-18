// kotlin
package com.example.bookshelf.network.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val items: List<VolumeItem>?
)

data class VolumeItem(
    val id: String,
    val volumeInfo: VolumeInfo?
)

data class VolumeInfo(
    val title: String?,
    val description: String?,
    val authors: List<String>?,
    val imageLinks: ImageLinks?,
    @SerializedName("averageRating")
    val averageRating: Double?,
    @SerializedName("ratingsCount")
    val ratingsCount: Int?,
    @SerializedName("publishedDate")
    val publishedDate: String?,
    @SerializedName("pageCount")
    val pageCount: Int?,
    val categories: List<String>?
)
