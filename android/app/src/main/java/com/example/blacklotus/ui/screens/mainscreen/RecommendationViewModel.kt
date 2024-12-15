package com.example.blacklotus.ui.screens.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blacklotus.data.repository.DiscountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val discountRepository: DiscountRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiEvent>(UiEvent.None)
    val uiState: StateFlow<UiEvent> = _uiState

    fun loadRecommendations(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiEvent.Loading
            try {
                val recommendations = discountRepository.loadRecommendations(id = id)
                _uiState.value = UiEvent.Success(recommendations)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    _uiState.value = UiEvent.Error("Пользователя с таким ID не существует")
                } else {
                    Log.e("fetch recommendations", "Ошибка при получении рекомендаций: ${e.message()}")
                    _uiState.value = UiEvent.Error("Ошибка при получении рекомендаций")
                }
            } catch (e: Exception) {
                Log.e("fetch recommendations", "Ошибка: ${e.message}")
                _uiState.value = UiEvent.Error("Ошибка при получении рекомендаций")
            }
        }
    }
}