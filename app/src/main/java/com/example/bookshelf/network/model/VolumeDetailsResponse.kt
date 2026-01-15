package com.example.bookshelf.network.model

data class VolumeDetailsResponse(
    val id: String,
    val volumeInfo: VolumeInfoDetails?
)

data class VolumeInfoDetails(
    val title: String?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String?
)