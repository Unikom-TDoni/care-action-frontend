package edu.rpl.careaction.feature.news.domain.dto.response

import com.google.gson.annotations.SerializedName
import edu.rpl.careaction.feature.news.domain.entity.NewsOverview

data class NewsOverviewResponse(
    val data: Collection<Data>
){
    data class Data(
        @SerializedName("id_news")
        val id: Int,
        val title: String,
        val thumbnail: String
    )
}

fun NewsOverviewResponse.toNewsOverviews(): Collection<NewsOverview> =
    data.map { NewsOverview(it.id, it.title, it.thumbnail) }