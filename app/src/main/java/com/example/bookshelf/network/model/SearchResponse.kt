package com.example.bookshelf.network.model

data class SearchResponse(
    val items: List<VolumeItem>?
)

data class VolumeItem(
    val id: String,
    val volumeInfo: VolumeInfo?
)

data class VolumeInfo(
    val title: String?
)