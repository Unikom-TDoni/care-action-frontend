package edu.rpl.careaction.feature.news.domain.dto.response

import com.google.gson.annotations.SerializedName
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.feature.news.domain.entity.NewsDetail

data class NewsDetailResponse(
    val data: Data
) {
    data class Data(
        val user: User,
        val title: String,
        val content: String,
        val thumbnail: String,
        @SerializedName("category_name") val name: String,
        @SerializedName("created_at") val createdDate: String,
    ) {
        data class User(
            val name: String
        )
    }
}

fun NewsDetailResponse.toNewsDetail(): NewsDetail =
    NewsDetail(
        data.title,
        data.user.name,
        data.content,
        data.name,
        DateUtility.convertStringToTimestamp(data.createdDate),
        data.thumbnail
    )
