package com.plotting.server.plogging.dto.response

import com.example.plotting_fe.plogging.dto.PloggingType
import java.math.BigDecimal

data class PloggingMapResponse(
    val ploggingId: Long,
    val title: String,
    val currentPeople: Long,
    val maxPeople: Long,
    val ploggingType: PloggingType,
    val recruitEndDate: String,
    val startTime: String,
    val spendTime: Long,
    val startLocation: String,
    val startLatitude: BigDecimal,
    val startLongitude: BigDecimal,
    val isStar : Boolean,
)




