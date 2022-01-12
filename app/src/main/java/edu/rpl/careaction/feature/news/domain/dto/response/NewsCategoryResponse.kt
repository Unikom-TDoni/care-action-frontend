package edu.rpl.careaction.feature.news.domain.dto.response

import edu.rpl.careaction.feature.news.domain.entity.NewsCategory

data class NewsCategoryResponse(
    val data: Collection<Data>
) {
    data class Data(
        val icon: String,
        val id_category: Int,
        val category_name: String,
    )
}

fun NewsCategoryResponse.toNewsCategories(): Collection<NewsCategory> =
    data.map { NewsCategory(it.id_category, it.icon, it.category_name) }