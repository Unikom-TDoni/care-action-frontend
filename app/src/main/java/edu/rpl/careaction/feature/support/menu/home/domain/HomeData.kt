package edu.rpl.careaction.feature.support.menu.home.domain

import edu.rpl.careaction.feature.news.domain.entity.NewsCategory
import edu.rpl.careaction.feature.news.domain.entity.NewsOverview
import edu.rpl.careaction.feature.motivation.domain.entity.Motivation
import edu.rpl.careaction.feature.user.domain.entity.User

data class HomeData(
    val user: User,
    val motivation: Motivation,
    val newsCategories: Collection<NewsCategory>,
    val newsOverview: Collection<NewsOverview>
)