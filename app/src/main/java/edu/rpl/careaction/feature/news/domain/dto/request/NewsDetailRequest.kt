package edu.rpl.careaction.feature.news.domain.dto.request

import com.google.gson.annotations.SerializedName

data class NewsDetailRequest(
    @SerializedName("id_news") val id: Int
)