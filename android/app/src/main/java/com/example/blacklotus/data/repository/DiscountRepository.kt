package com.example.blacklotus.data.repository

import com.example.blacklotus.data.models.Reccomendation
import com.example.blacklotus.data.remote.data_source.DiscountDataSource
import javax.inject.Inject

interface DiscountRepository {
    suspend fun loadRecommendations(id: Int): List<Reccomendation>
}

class DiscountRepositoryImp @Inject constructor(
    private val discountDataSource: DiscountDataSource
) : DiscountRepository {
    override suspend fun loadRecommendations(id: Int): List<Reccomendation> = discountDataSource.loadRecommendations(id = id)
}
