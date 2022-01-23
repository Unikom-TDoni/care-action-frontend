package edu.rpl.careaction.feature.activity_tracker.domain.dto.request

import java.util.*

data class ActivityTrackerRequest(
    val date: Date,
    val checked: Boolean? = null,
    val id_activity: Int? = null,
)