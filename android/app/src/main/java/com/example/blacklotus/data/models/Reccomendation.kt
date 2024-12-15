package com.example.blacklotus.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Reccomendation(
    val title: String,
    val body: String
)
