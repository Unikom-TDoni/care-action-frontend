package edu.rpl.careaction.feature.activity_tracker.domain.entity

data class ActivityTracker(
    val id: Int,
    val icon: String,
    val tittle: String,
    var isChecked: Boolean,
)
