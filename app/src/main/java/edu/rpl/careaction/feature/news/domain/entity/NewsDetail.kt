package edu.rpl.careaction.feature.news.domain.entity

import java.util.*

data class NewsDetail(
    val title: String,
    val author: String,
    val content: String,
    val category: String,
    val releaseDate: Date,
    val thumbnail: String,
)