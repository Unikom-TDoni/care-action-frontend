package edu.rpl.careaction.feature.motivation.domain.dto

import edu.rpl.careaction.feature.motivation.domain.entity.Motivation

data class MotivationResponse(
    val data: Collection<Data>
){
    data class Data(
        val id: Int,
        val quotes: String,
        val creator: String
    )
}

fun MotivationResponse.toMotivation(): Motivation =
    Motivation(
        data.first().quotes
    )