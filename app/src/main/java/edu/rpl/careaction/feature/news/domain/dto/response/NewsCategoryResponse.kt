package edu.rpl.careaction.feature.news.domain.dto.response

import com.google.gson.annotations.SerializedName
import edu.rpl.careaction.feature.news.domain.entity.NewsCategory

data class NewsCategoryResponse(
    val data: Collection<Data>
) {
    data class Data(
        val icon: String,
        @SerializedName("id_category") val id: Int,
        @SerializedName("category_name") val name: String,
    )
}

fun NewsCategoryResponse.toNewsCategories(): Collection<NewsCategory> =
    data.map { NewsCategory(it.id, it.icon, it.name) }