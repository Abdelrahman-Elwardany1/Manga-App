package com.example.mangaapp.features.search.domain.usecases

import com.example.mangaapp.features.search.domain.TagRepository
import com.example.mangaapp.features.search.domain.models.Tag

class GetTagListUseCase(private val repository: TagRepository) {
    suspend operator fun invoke(): Result<List<Tag>> {
        return repository.getTagList()
    }
}