package com.plotting.server.plogging.dto.response

import java.math.BigDecimal
import java.io.Serializable

data class PloggingMapResponse(
    val ploggingId: Long,
    val title: String,
    val currentPeople: Long,
    val maxPeople: Long,
    val ploggingType: String,
    val recruitEndDate: String,
    val startTime: String,
    val spendTime: Long,
    val startLocation: String,
    val startLatitude: BigDecimal,
    val startLongitude: BigDecimal
) : Serializable



