package com.example.plotting_fe.global.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GeocodeResponse (
    val status: String,
    val meta: Meta,
    val addresses: List<Addresses>
) {
    data class Meta(
        val totalCount: Int,
        val page: Int,
        val count: Int
    )
    data class Addresses(
        val roadAddress: String
    )
}


