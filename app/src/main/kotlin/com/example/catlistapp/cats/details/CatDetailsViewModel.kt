package com.example.catlistapp.cats.details


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.details.model.CatDetailsUiModel
import com.example.catlistapp.cats.gallery.photo.catId
import com.example.catlistapp.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class CatDetailsViewModel @Inject constructor(

    savedStateHandle: SavedStateHandle,
    private val catsRepository: CatsRepository
): ViewModel(){

    private val catId: String = savedStateHandle.catId
    private val _state = MutableStateFlow(CatDetailsContract.CatDetailsState(catId = catId))
    val state = _state.asStateFlow()
    private fun setState(reducer: CatDetailsContract.CatDetailsState.() -> CatDetailsContract.CatDetailsState) = _state.update(reducer)

    init{
        observeCatDetails()
    }



    private fun observeCatDetails() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            catsRepository.getCatByIdFlow(id = catId).collect { catInfoDetail ->
                setState { copy(cat = catInfoDetail, loading = false) }
            }
        }
    }


    private fun CatApiModel.asCatDetailsUiModel() = CatDetailsUiModel(
        id = this.id,
        name=this.name,
        temperament=this.temperament,
        origin=this.origin,
        description=this.description,
        life_span=this.life_span,
        alt_names=this.alt_names,
        adaptability=this.adaptability,
        affection_level=this.affection_level,
        stranger_friendly=this.stranger_friendly,
        dog_friendly=this.dog_friendly,
        energy_level=this.energy_level,
        social_needs=this.social_needs,
        health_issues=this.health_issues,
        intelligence=this.intelligence,
        rare = this.rare,
        wikipedia_url= this.wikipedia_url,
        reference_image_id = this.reference_image_id,
        weight=this.weight.metric
    )
}