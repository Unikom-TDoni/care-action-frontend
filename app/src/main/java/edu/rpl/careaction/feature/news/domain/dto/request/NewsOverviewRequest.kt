package edu.rpl.careaction.feature.news.domain.dto.request

import com.google.gson.annotations.SerializedName

data class NewsOverviewRequest(
    @SerializedName("id_category") val id: Int?
)