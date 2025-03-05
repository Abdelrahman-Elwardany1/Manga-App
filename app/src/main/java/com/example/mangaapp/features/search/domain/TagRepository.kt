package com.example.mangaapp.features.search.domain

import com.example.mangaapp.features.search.domain.models.Tag

interface TagRepository {
    suspend fun getTagList(): Result<List<Tag>>
}