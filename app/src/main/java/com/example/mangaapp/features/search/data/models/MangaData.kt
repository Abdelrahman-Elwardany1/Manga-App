package com.example.mangaapp.features.search.data.models

data class MangaData(
    val id: String,
    val attributes: MangaAttributes,
    val relationships: List<MangaRelationship>
)