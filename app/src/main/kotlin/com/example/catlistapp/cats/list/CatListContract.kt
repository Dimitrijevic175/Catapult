package com.example.catlistapp.cats.list

import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.list.model.CatListUiModel

interface CatListContract {

    data class CatListState(
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val cats: List<CatApiModel> = emptyList(),
        val filteredCats: List<CatApiModel> = emptyList(),
    )

    sealed class CatListUiEvent {
        data class SearchQueryChanged(val query: String) : CatListUiEvent()
        data object CloseSearchMode : CatListUiEvent()

    }
}