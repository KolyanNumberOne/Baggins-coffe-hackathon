package com.example.blacklotus.ui.screens.mainscreen

import com.example.blacklotus.data.models.Reccomendation

sealed class UiEvent {
    object Loading : UiEvent()
    data class Success(val recommendations: List<Reccomendation>) : UiEvent()
    data class Error(val message: String) : UiEvent()
    object None : UiEvent()
}